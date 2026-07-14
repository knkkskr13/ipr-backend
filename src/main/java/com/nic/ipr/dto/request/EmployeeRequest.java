package com.nic.ipr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRequest {
    @NotBlank
    private String name;
    private String email;
    private String service;
    private String lengthOfService;
    private String presentPostHeld;
    private String placeOfPosting;
    @NotNull
    private Long officeId;
}