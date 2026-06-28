package com.nic.ipr.service;

import com.nic.ipr.dto.request.IprNotificationRequest;
import com.nic.ipr.dto.response.IprNotificationResponse;

import java.util.List;

public interface IprNotificationService {

    List<IprNotificationResponse> getAllNotifications();

    IprNotificationResponse getActiveNotification();

    IprNotificationResponse createNotification(IprNotificationRequest request);

    IprNotificationResponse updateNotification(Long id, IprNotificationRequest request);

    void deleteNotification(Long id);

    boolean isFilingWindowOpen(); // used internally, no response DTO needed
}