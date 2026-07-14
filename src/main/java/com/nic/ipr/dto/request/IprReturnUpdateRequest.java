package com.nic.ipr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class IprReturnUpdateRequest {
    @NotBlank
    private String reportingYear;
    @NotNull
    private LocalDate asOnDate;
    @NotNull
    private BigDecimal totalAnnualIncome;
    @NotNull
    private Boolean isNoProperty;
}