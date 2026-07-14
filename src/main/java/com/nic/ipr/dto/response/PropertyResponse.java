package com.nic.ipr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {
    private Long id;
    private Long iprReturnId;
    private String locationAddress;
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