package com.nic.ipr.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IprDeclarationResponse {

    private Long declarationId;
    private Long iprId;                     // just the ID, not full IprReturn object

    private String declarationText;
    private Boolean agreed;
    private LocalDate declarationDate;
    private String place;
    private String employeeSignature;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}