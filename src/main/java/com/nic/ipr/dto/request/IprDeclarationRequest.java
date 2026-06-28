package com.nic.ipr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IprDeclarationRequest {

    @NotNull(message = "IPR Return ID is required")
    private Long iprId;                     // Long IprId

    private String declarationText;         //Predefined text -- in UI == "I hereby declare that..."

    @NotNull(message = "You must agree to the declaration")
    private Boolean agreed;                 // must be true to submit

    @NotNull(message = "Declaration date is required")
    private LocalDate declarationDate;              //Date calender selection

    @NotBlank(message = "Place is required")
    private String place;                      //String

    @NotBlank(message = "Signature is required")
    private String employeeSignature;       // String name as signature
}