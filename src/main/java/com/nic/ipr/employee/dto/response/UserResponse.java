package com.nic.ipr.employee.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String role;

    // Employee details
    private Long employeeId;
    private String employeeName;
    private String email;

    // Department & Office details
    private Long departmentId;
    private String departmentName;
    private Long officeId;
    private String officeName;
}