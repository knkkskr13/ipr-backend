package com.nic.ipr.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IprDeclarationRequest {
    @NotNull
    private Long iprReturnId;
    private String declarationText;
    private Boolean agreed;
    private LocalDate declarationDate;
    private String place;
    private String employeeSignature;
}