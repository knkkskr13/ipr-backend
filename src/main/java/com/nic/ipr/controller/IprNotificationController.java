package com.nic.ipr.controller;

import com.nic.ipr.entity.IprNotification;
import com.nic.ipr.service.IprNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ipr-notification")
@RequiredArgsConstructor
public class IprNotificationController {

    private final IprNotificationService service;

    @GetMapping("/get")
    public List<IprNotification> getAllNotifications() {
        return service.getAllNotifications();
    }

    @GetMapping("/get/active")
    public IprNotification getActiveNotification() {
        return service.getActiveNotification();
    }

    @PostMapping("/add")
    public IprNotification addNotification(@RequestBody IprNotification notification) {
        return service.createNotification(notification);
    }

    @PutMapping("/update/{id}")
    public IprNotification updateNotification(@PathVariable Long id,
                                              @RequestBody IprNotification notification) {
        return service.updateNotification(id, notification);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteNotification(@PathVariable Long id) {
        service.deleteNotification(id);
    }
}
