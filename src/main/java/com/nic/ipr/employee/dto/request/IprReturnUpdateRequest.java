package com.nic.ipr.employee.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IprReturnUpdateRequest {
    private String reportingYear;
    private LocalDate asOnDate;
    private Double totalAnnualIncome;
    private Boolean isNoProperty;
}