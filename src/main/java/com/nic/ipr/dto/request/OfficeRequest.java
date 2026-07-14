package com.nic.ipr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OfficeRequest {
    @NotBlank
    private String name;
    @NotNull
    private Long departmentId;
}