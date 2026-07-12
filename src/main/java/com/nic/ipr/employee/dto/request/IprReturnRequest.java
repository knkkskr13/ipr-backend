package com.nic.ipr.employee.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IprReturnRequest {
    private Long employeeId;
    private String reportingYear;
    private LocalDate asOnDate;
    private Double totalAnnualIncome;
    private Boolean isNoProperty;
}