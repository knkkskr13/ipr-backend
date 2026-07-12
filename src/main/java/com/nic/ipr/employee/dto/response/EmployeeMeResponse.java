package com.nic.ipr.employee.dto.response;

import lombok.Data;

@Data
public class EmployeeMeResponse {
    private Long userId;
    private String username;
    private String role;
    private Long employeeId;
    private String name;
    private String email;
    private String department;
    private String service;
    private String lengthOfService;
    private String presentPostHeld;
    private String placeOfPosting;
    private Long officeId;
    private String officeName;
}