package com.nic.ipr.hod.service;

import com.nic.ipr.hod.dto.request.IprNotificationRequest;
import com.nic.ipr.hod.dto.request.IprNotificationUpdateRequest;
import com.nic.ipr.hod.dto.response.IprNotificationResponse;

import java.util.List;

public interface IprNotificationService {
    List<IprNotificationResponse> getAllNotifications();
    IprNotificationResponse getActiveNotificationByOffice(Long officeId);
    List<IprNotificationResponse> getNotificationsByOffice(Long officeId);
    IprNotificationResponse createNotification(IprNotificationRequest request);
    IprNotificationResponse updateNotification(Long id, IprNotificationUpdateRequest request);
    void deleteNotification(Long id);

    List<IprNotificationResponse> getAllActiveNotifications();
}