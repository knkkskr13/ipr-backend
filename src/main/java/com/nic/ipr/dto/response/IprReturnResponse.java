package com.nic.ipr.dto.response;

import com.nic.ipr.status.IprStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IprReturnResponse {

    private Long iprId;
    private String reportingYear;
    private LocalDate asOnDate;
    private BigDecimal totalAnnualIncome;
    private Boolean isNoProperty;
    private IprStatus status;               // DRAFT / SUBMITTED / APPROVED / RETURNED

    // Employee info (flattened — no need to send full Employee object)
    private Long employeeId;
    private String employeeName;
    private String employeeDepartment;
    private String employeePresentPostHeld;

    // Timestamps
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}