package com.nic.ipr.hod.implementation;

import com.nic.ipr.hod.dto.request.IprNotificationRequest;
import com.nic.ipr.hod.dto.request.IprNotificationUpdateRequest;
import com.nic.ipr.hod.dto.response.IprNotificationResponse;
import com.nic.ipr.hod.entity.IprNotification;
import com.nic.ipr.hod.entity.Office;
import com.nic.ipr.hod.repository.IprNotificationRepository;
import com.nic.ipr.hod.repository.OfficeRepository;
import com.nic.ipr.hod.service.IprNotificationService;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import com.nic.ipr.employee.entity.User;
import com.nic.ipr.employee.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IprNotificationServiceImpl implements IprNotificationService {

    private static final long MAX_CREATE_WINDOW_DAYS = 45;
    private static final long MAX_UPDATE_EXTENSION_DAYS = 15;

    private final IprNotificationRepository iprNotificationRepository;
    private final OfficeRepository officeRepository;
    private final UserRepository userRepository;

    private User getCurrentHodUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (!"HOD".equals(user.getRole())) {
            throw new BadRequestException("Access denied: Requires HOD privileges");
        }

        // Simplified check! We only need to ensure the Employee record exists.
        if (user.getEmployee() == null) {
            throw new BadRequestException("HOD account is not assigned to an employee record.");
        }
        return user;
    }

    private IprNotificationResponse mapToResponse(IprNotification notification) {
        IprNotificationResponse response = new IprNotificationResponse();
        response.setId(notification.getId());
        response.setSession(notification.getSession());
        response.setMessage(notification.getMessage());
        response.setStartDate(notification.getStartDate());
        response.setEndDate(notification.getEndDate());
        response.setActive(notification.getActive());
        response.setOfficeId(notification.getOffice().getId());
        response.setOfficeName(notification.getOffice().getName());
        response.setCreatedAt(notification.getCreatedAt());
        response.setUpdatedAt(notification.getUpdatedAt());
        return response;
    }

    @Override
    public List<IprNotificationResponse> getAllNotifications() {
        User hodUser = getCurrentHodUser();
        // Direct, confident access to the Department ID!
        Long hodDepartmentId = hodUser.getEmployee().getDepartment().getId();
        return iprNotificationRepository.findByOfficeDepartmentId(hodDepartmentId)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public IprNotificationResponse getActiveNotificationByOffice(Long officeId) {
        return iprNotificationRepository.findByOfficeIdAndActiveTrue(officeId)
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Override
    public List<IprNotificationResponse> getNotificationsByOffice(Long officeId) {
        if (!officeRepository.existsById(officeId)) {
            throw new ResourceNotFoundException("Office not found with id: " + officeId);
        }
        return iprNotificationRepository.findByOfficeId(officeId).stream().map(this::mapToResponse).toList();
    }

    @Override
    public IprNotificationResponse createNotification(IprNotificationRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date cannot be before start date");
        }

        long windowDays = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (windowDays > MAX_CREATE_WINDOW_DAYS) {
            throw new BadRequestException(
                    "Filing window cannot exceed " + MAX_CREATE_WINDOW_DAYS + " days. You selected " + windowDays + " days."
            );
        }

        Office office = officeRepository.findById(request.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found with id: " + request.getOfficeId()));

        User hodUser = getCurrentHodUser();
        if (!office.getDepartment().getId().equals(hodUser.getEmployee().getDepartment().getId())) {
            throw new BadRequestException("You can only create notifications for offices in your department");
        }

        if (iprNotificationRepository.findByOfficeIdAndActiveTrue(request.getOfficeId()).isPresent()) {
            throw new BadRequestException("An active notification already exists for this office");
        }

        boolean sessionAlreadyExists  = iprNotificationRepository
                .findByOfficeId(request.getOfficeId())
                .stream()
                .anyMatch(notification -> notification.getSession().equals(request.getSession()));

        if (sessionAlreadyExists ) {
            throw new BadRequestException(
                    "A notification for session " + request.getSession() +
                            " already exists for this office. Edit the existing one instead."
            );
        }

        IprNotification notification = new IprNotification();
        notification.setSession(request.getSession());
        notification.setMessage(request.getMessage());
        notification.setStartDate(request.getStartDate());
        notification.setEndDate(request.getEndDate());
        notification.setActive(request.getActive() != null ? request.getActive() : true);
        notification.setOffice(office);

        return mapToResponse(iprNotificationRepository.save(notification));
    }

    @Override
    public IprNotificationResponse updateNotification(Long id, IprNotificationUpdateRequest request) {
        IprNotification existing = iprNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        if (request.getEndDate().isBefore(existing.getEndDate())) {
            throw new BadRequestException(
                    "You cannot reduce the filing window end date. Current end date: " + existing.getEndDate()
            );
        }

        long totalDays = ChronoUnit.DAYS.between(existing.getStartDate(), request.getEndDate());
        if (totalDays > MAX_CREATE_WINDOW_DAYS + MAX_UPDATE_EXTENSION_DAYS) {
            throw new BadRequestException(
                    "Total filing window cannot exceed " + (MAX_CREATE_WINDOW_DAYS + MAX_UPDATE_EXTENSION_DAYS) +
                            " days from the original start date (" + existing.getStartDate() + ")."
            );
        }
        Office office = officeRepository.findById(request.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found with id: " + request.getOfficeId()));

        User hodUser = getCurrentHodUser();
        // Direct department access!
        Long hodDepartmentId = hodUser.getEmployee().getDepartment().getId();
        if (!office.getDepartment().getId().equals(hodDepartmentId)) {
            throw new BadRequestException("You can only manage notifications for offices in your department");
        }

        boolean officeChanging = !existing.getOffice().getId().equals(request.getOfficeId());
        boolean becomingActive = !existing.getActive() && Boolean.TRUE.equals(request.getActive());

        if ((officeChanging || becomingActive) &&
                iprNotificationRepository.findByOfficeIdAndActiveTrue(request.getOfficeId()).isPresent()) {
            throw new BadRequestException("An active notification already exists for this office");
        }

        existing.setSession(request.getSession());
        existing.setMessage(request.getMessage());
        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());
        existing.setActive(request.getActive() != null ? request.getActive() : existing.getActive());
        existing.setOffice(office);

        return mapToResponse(iprNotificationRepository.save(existing));
    }

    @Override
    public void deleteNotification(Long id) {
        IprNotification iprNotification = iprNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        User hodUser = getCurrentHodUser();
        // Direct department access!
        Long hodDepartmentId = hodUser.getEmployee().getDepartment().getId();
        if (!iprNotification.getOffice().getDepartment().getId().equals(hodDepartmentId)) {
            throw new BadRequestException("You can only delete notifications from your own department");
        }

        iprNotificationRepository.deleteById(id);
    }

    @Override
    public List<IprNotificationResponse> getAllActiveNotifications() {
        return iprNotificationRepository.findByActiveTrue()
                .stream().map(this::mapToResponse).toList();
    }
}