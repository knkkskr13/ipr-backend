package com.nic.ipr.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeRequest {

    @NotBlank(message = "Employee name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Service category is required")
    private String service;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Length of service is required")
    private String lengthOfService;

    @NotBlank(message = "Present post held designation is required")
    private String presentPostHeld;

    @NotBlank(message = "Place of posting is required")
    private String placeOfPosting;
}