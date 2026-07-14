package com.nic.ipr.service;

import com.nic.ipr.dto.request.IprNotificationRequest;
import com.nic.ipr.dto.request.IprNotificationUpdateRequest;
import com.nic.ipr.dto.response.IprNotificationResponse;
import java.util.List;

public interface IprNotificationService {
    IprNotificationResponse createNotification(IprNotificationRequest request);
    IprNotificationResponse updateNotification(Long id, IprNotificationUpdateRequest request);
    void deleteNotification(Long id);
    List<IprNotificationResponse> getAllNotifications();
    List<IprNotificationResponse> getNotificationsByOffice(Long officeId);
    IprNotificationResponse getActiveNotificationByOffice(Long officeId);
    List<IprNotificationResponse> getAllActiveNotifications();
    boolean isFilingWindowOpen(Long officeId);
}