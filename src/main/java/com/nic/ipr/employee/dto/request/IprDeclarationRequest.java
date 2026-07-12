package com.nic.ipr.employee.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IprDeclarationRequest {
    private Long iprReturnId;
    private String declarationText;
    private Boolean agreed;
    private LocalDate declarationDate;
    private String place;
    private String employeeSignature;
}