package com.nic.ipr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class IprReturnUpdateRequest {

    @NotBlank(message = "Reporting year is required")
    private String reportingYear;

    @NotNull(message = "As on date is required")
    private LocalDate asOnDate;

    private BigDecimal totalAnnualIncome;

    private Boolean isNoProperty = false;
}