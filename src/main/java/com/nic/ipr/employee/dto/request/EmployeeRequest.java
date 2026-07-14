package com.nic.ipr.employee.dto.request;

import lombok.Data;

@Data
public class EmployeeRequest {
    private String name;
    private String email;
    private String service;
    private String lengthOfService;
    private String presentPostHeld;
    private String placeOfPosting;
    private Long officeId;
}