package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.IprNotificationRequest;
import com.nic.ipr.dto.response.IprNotificationResponse;
import com.nic.ipr.entity.IprNotification;
import com.nic.ipr.exception.BadRequestException;
import com.nic.ipr.exception.ResourceNotFoundException;
import com.nic.ipr.repository.IprNotificationRepository;
import com.nic.ipr.service.IprNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IprNotificationServiceImpl implements IprNotificationService {

    private final IprNotificationRepository repository;

    // ----------------------------------------------------------------
    // MAPPER METHODS
    // ----------------------------------------------------------------

    private IprNotification mapToEntity(IprNotificationRequest request) {
        IprNotification notification = new IprNotification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setStartDate(request.getStartDate());
        notification.setEndDate(request.getEndDate());
        notification.setActive(request.getActive() != null ? request.getActive() : true);
        return notification;
    }

    private IprNotificationResponse mapToResponse(IprNotification notification) {
        IprNotificationResponse response = new IprNotificationResponse();
        response.setId(notification.getId());
        response.setTitle(notification.getTitle());
        response.setMessage(notification.getMessage());
        response.setStartDate(notification.getStartDate());
        response.setEndDate(notification.getEndDate());
        response.setActive(notification.getActive());
        response.setCreatedAt(notification.getCreatedAt());
        response.setUpdatedAt(notification.getUpdatedAt());
        return response;
    }

    // ----------------------------------------------------------------
    // SERVICE METHODS
    // ----------------------------------------------------------------

    @Override
    public List<IprNotificationResponse> getAllNotifications() {
        return repository.findAll()
                .stream()
                .map(notification -> mapToResponse(notification))
                .toList();
    }

    @Override
    public IprNotificationResponse getActiveNotification() {
        IprNotification notification = repository.findByActiveTrue()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No active notification found"));
        return mapToResponse(notification);
    }

    @Override
    public IprNotificationResponse createNotification(IprNotificationRequest request) {
        Optional<IprNotification> existing = repository.findByActiveTrue();
        if (existing.isPresent()) {
            throw new BadRequestException(
                    "An active filing window already exists");
        }

        IprNotification notification = mapToEntity(request);
        IprNotification saved = repository.save(notification);
        return mapToResponse(saved);
    }

    @Override
    public IprNotificationResponse updateNotification(Long id, IprNotificationRequest request) {
        IprNotification existing = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notification not found with id: " + id));

        existing.setTitle(request.getTitle());
        existing.setMessage(request.getMessage());
        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());
        existing.setActive(request.getActive() != null ? request.getActive() : existing.getActive());

        IprNotification saved = repository.save(existing);
        return mapToResponse(saved);
    }

    @Override
    public void deleteNotification(Long id) {
        repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notification not found with id: " + id));
        repository.deleteById(id);
    }

    @Override
    public boolean isFilingWindowOpen() {
        Optional<IprNotification> current = repository.findByActiveTrue();

        if (!current.isPresent()) return false;

        IprNotification notification = current.get();
        LocalDate today = LocalDate.now();

        if (today.isBefore(notification.getStartDate())) return false;
        if (today.isAfter(notification.getEndDate())) return false;

        return true;
    }
}