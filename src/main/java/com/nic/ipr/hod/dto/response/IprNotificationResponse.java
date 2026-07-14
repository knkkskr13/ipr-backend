package com.nic.ipr.hod.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IprNotificationResponse {
    private Long id;
    private String session;
    private String message;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private Long officeId;
    private String officeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}