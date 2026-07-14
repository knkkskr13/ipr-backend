package com.nic.ipr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyRequest {
    @NotNull
    private Long iprReturnId;
    @NotBlank
    private String locationAddress;
    @NotBlank
    private String propertyType;
    private String propertyDescription;
    private BigDecimal acquisitionCost;
    private String acquisitionYear;
    private BigDecimal presentValue;
    private String ownerName;
    private String ownerRelation;
    private String acquisitionMode;
    private String acquisitionDetails;
    private BigDecimal annualIncome;
    private String remarks;
}