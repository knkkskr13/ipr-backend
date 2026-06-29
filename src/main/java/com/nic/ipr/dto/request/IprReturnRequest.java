package com.nic.ipr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class IprReturnRequest {
    @NotNull(message = "EmployeeId is Required")
    private Long employeeId;

    @NotBlank(message = "Reporting year is Requried")
    private String reportingYear;

    @NotNull(message = "As on date is required")
    private LocalDate asOnDate;

    @NotNull(message = "Annual Income Must be Disclosed")
    private BigDecimal totalAnnualIncome;

    private Boolean isNoProperty = false;
}
