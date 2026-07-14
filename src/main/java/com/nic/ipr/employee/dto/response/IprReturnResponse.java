package com.nic.ipr.employee.dto.response;

import com.nic.ipr.shared.status.IprStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IprReturnResponse {
    private Long iprId;
    private Long employeeId;
    private String employeeName;
    private String employeeDepartment;
    private String employeePresentPostHeld;
    private String reportingYear;
    private LocalDate asOnDate;
    private Double totalAnnualIncome;
    private Boolean isNoProperty;
    private IprStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}