package com.nic.ipr.hod.controller;

import com.nic.ipr.hod.dto.request.IprNotificationRequest;
import com.nic.ipr.hod.dto.request.IprNotificationUpdateRequest;
import com.nic.ipr.hod.dto.response.IprNotificationResponse;
import com.nic.ipr.hod.service.IprNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ipr-notification")
@RequiredArgsConstructor
public class IprNotificationController {

    private final IprNotificationService iprNotificationService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('HOD')")
    public List<IprNotificationResponse> getAllNotifications() {
        return iprNotificationService.getAllNotifications();
    }

    @GetMapping("/get/active")
    @PreAuthorize("hasRole('HOD')")
    public List<IprNotificationResponse> getAllActiveNotifications() {
        return iprNotificationService.getAllActiveNotifications();
    }

    @GetMapping("/get/active/{officeId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IprNotificationResponse> getActiveByOffice(@PathVariable Long officeId) {

        IprNotificationResponse response = iprNotificationService.getActiveNotificationByOffice(officeId);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/office/{officeId}")
    @PreAuthorize("hasRole('HOD')")
    public List<IprNotificationResponse> getByOffice(@PathVariable Long officeId) {
        return iprNotificationService.getNotificationsByOffice(officeId);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('HOD')")
    public IprNotificationResponse createNotification(@Valid @RequestBody IprNotificationRequest request) {
        return iprNotificationService.createNotification(request);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('HOD')")
    public IprNotificationResponse updateNotification(@PathVariable Long id, @Valid @RequestBody IprNotificationUpdateRequest request) {
        return iprNotificationService.updateNotification(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('HOD')")
    public void deleteNotification(@PathVariable Long id) {
        iprNotificationService.deleteNotification(id);
    }
}