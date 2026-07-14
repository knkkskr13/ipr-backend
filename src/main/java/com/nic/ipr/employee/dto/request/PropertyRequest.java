package com.nic.ipr.employee.dto.request;

import lombok.Data;

@Data
public class PropertyRequest {
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
}