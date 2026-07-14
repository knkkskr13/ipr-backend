package com.nic.ipr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IprNotificationResponse {
    private Long id;
    private String session;
    private String message;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private Long officeId;
    private String officeName;
    private Long departmentId;
    private String departmentName;
}