package com.nic.ipr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyRequest {

    @NotNull(message = "IPR Return ID is required")
    private Long iprId;                   // which IprReturn this property belongs to

    @NotBlank(message = "Location address is required")
    private String locationAddress;                     //string not null

    @NotBlank(message = "Property type is required")
    private String propertyType;        //not null string

    private String propertyDescription;     //String

    private BigDecimal acquisitionCost;         //Bigdecimal

    private Integer acquisitionYear;            //Integer

    private BigDecimal presentValue;            //Bigdecimal

    @NotBlank(message = "Owner name is required")  //String text not null
    private String ownerName;

    @NotBlank(message = "Owner relation is required")
    private String ownerRelation;       // DropDown --  SELF / SPOUSE / DEPENDENT

    private String acquisitionMode;     //  DropDown --  e.g. Purchase / Inheritance / Gift

    private String acquisitionDetails;         //String text

    private BigDecimal annualIncome;        // Bigdecimal

    private String remarks;                 //String text
}