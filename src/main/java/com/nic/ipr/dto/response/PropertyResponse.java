package com.nic.ipr.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PropertyResponse {

    private Long propertyId;
    private Long iprId;                     // just the ID, not full IprReturn object

    private String locationAddress;
    private String propertyType;
    private String propertyDescription;

    private BigDecimal acquisitionCost;
    private Integer acquisitionYear;
    private BigDecimal presentValue;

    private String ownerName;
    private String ownerRelation;

    private String acquisitionMode;
    private String acquisitionDetails;

    private BigDecimal annualIncome;
    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}