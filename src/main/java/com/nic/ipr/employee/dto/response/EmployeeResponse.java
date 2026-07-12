package com.nic.ipr.employee.dto.response;

import lombok.Data;

@Data
public class EmployeeResponse {
    private Long id;
    private String name;
    private String email;
    private String service;
    private String departmentName;
    private String lengthOfService;
    private String presentPostHeld;
    private String placeOfPosting;
    private Long officeId;
    private String officeName;
}