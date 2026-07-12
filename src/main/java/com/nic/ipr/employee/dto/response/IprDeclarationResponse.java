package com.nic.ipr.employee.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IprDeclarationResponse {
    private Long id;
    private Long iprReturnId;
    private String declarationText;
    private Boolean agreed;
    private LocalDate declarationDate;
    private String place;
    private String employeeSignature;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}