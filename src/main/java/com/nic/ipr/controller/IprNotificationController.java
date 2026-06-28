package com.nic.ipr.controller;

import com.nic.ipr.dto.request.IprNotificationRequest;
import com.nic.ipr.dto.response.IprNotificationResponse;
import com.nic.ipr.service.IprNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ipr-notification")
@RequiredArgsConstructor
public class IprNotificationController {

    private final IprNotificationService service;

    @GetMapping("/get")
    public List<IprNotificationResponse> getAllNotifications() {
        return service.getAllNotifications();
    }

    @GetMapping("/get/active")
    public IprNotificationResponse getActiveNotification() {
        return service.getActiveNotification();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public IprNotificationResponse createNotification(
            @Valid @RequestBody IprNotificationRequest request) {
        return service.createNotification(request);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public IprNotificationResponse updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody IprNotificationRequest request) {
        return service.updateNotification(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteNotification(@PathVariable Long id) {
        service.deleteNotification(id);
    }
}