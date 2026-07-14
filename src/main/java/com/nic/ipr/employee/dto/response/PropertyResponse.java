package com.nic.ipr.employee.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PropertyResponse {
    private Long id;
    private Long iprReturnId;
    private String locationAddress;
    private String propertyType;
    private String propertyDescription;
    private Double acquisitionCost;
    private String acquisitionYear;
    private Double presentValue;
    private String ownerName;
    private String ownerRelation;
    private String acquisitionMode;
    private String acquisitionDetails;
    private Double annualIncome;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}