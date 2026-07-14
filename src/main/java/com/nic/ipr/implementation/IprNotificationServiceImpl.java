package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.IprNotificationRequest;
import com.nic.ipr.dto.request.IprNotificationUpdateRequest;
import com.nic.ipr.dto.response.IprNotificationResponse;
import com.nic.ipr.entity.IprNotification;
import com.nic.ipr.entity.Office;
import com.nic.ipr.repository.IprNotificationRepository;
import com.nic.ipr.repository.OfficeRepository;
import com.nic.ipr.service.IprNotificationService;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IprNotificationServiceImpl implements IprNotificationService {

    private final IprNotificationRepository notificationRepository;
    private final OfficeRepository officeRepository;

    @Override
    public IprNotificationResponse createNotification(IprNotificationRequest request) {
        Office office = officeRepository.findById(request.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found"));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date cannot be before start date");
        }

        if (notificationRepository.existsByOfficeIdAndActiveTrue(request.getOfficeId())) {
            throw new BadRequestException("An active notification already exists for this office");
        }

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days > 45) {
            throw new BadRequestException("Filing window cannot exceed 45 days");
        }

        IprNotification notification = new IprNotification();
        notification.setSession(request.getSession());
        notification.setMessage(request.getMessage());
        notification.setOffice(office);
        notification.setStartDate(request.getStartDate());
        notification.setEndDate(request.getEndDate());
        notification.setActive(request.getActive());

        return toResponse(notificationRepository.save(notification));
    }

    @Override
    public IprNotificationResponse updateNotification(Long id, IprNotificationUpdateRequest request) {
        IprNotification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (request.getEndDate().isBefore(notification.getEndDate())) {
            throw new BadRequestException("Cannot reduce the end date. Current end date: " + notification.getEndDate());
        }

        long days = ChronoUnit.DAYS.between(notification.getStartDate(), request.getEndDate());
        if (days > 60) {
            throw new BadRequestException("Total filing window cannot exceed 60 days from original start date");
        }

        notification.setSession(request.getSession());
        notification.setMessage(request.getMessage());
        notification.setEndDate(request.getEndDate());
        notification.setActive(request.getActive());

        return toResponse(notificationRepository.save(notification));
    }

    @Override
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found");
        }
        notificationRepository.deleteById(id);
    }

    @Override
    public List<IprNotificationResponse> getAllNotifications() {
        return notificationRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<IprNotificationResponse> getNotificationsByOffice(Long officeId) {
        return notificationRepository.findByOfficeId(officeId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public IprNotificationResponse getActiveNotificationByOffice(Long officeId) {
        return toResponse(notificationRepository.findByOfficeIdAndActiveTrue(officeId)
                .orElseThrow(() -> new ResourceNotFoundException("No active notification for this office")));
    }

    @Override
    public List<IprNotificationResponse> getAllActiveNotifications() {
        return notificationRepository.findByActive(true)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public boolean isFilingWindowOpen(Long officeId) {
        // Must have active=true AND today must be within startDate..endDate
        return notificationRepository.findByOfficeIdAndActiveTrue(officeId)
                .map(n -> {
                    LocalDate today = LocalDate.now();
                    return !today.isBefore(n.getStartDate()) && !today.isAfter(n.getEndDate());
                })
                .orElse(false);
    }

    private IprNotificationResponse toResponse(IprNotification n) {
        IprNotificationResponse response = new IprNotificationResponse();
        response.setId(n.getId());
        response.setSession(n.getSession());
        response.setMessage(n.getMessage());
        response.setStartDate(n.getStartDate());
        response.setEndDate(n.getEndDate());
        response.setActive(n.getActive());
        response.setOfficeId(n.getOffice().getId());
        response.setOfficeName(n.getOffice().getName());
        response.setDepartmentId(n.getOffice().getDepartment().getId());
        response.setDepartmentName(n.getOffice().getDepartment().getName());
        return response;
    }
}