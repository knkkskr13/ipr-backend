package com.nic.ipr.dto.response;

import lombok.Data;

@Data
public class EmployeeResponse {

    private Long id;
    private String name;
    private String email;
    private String service;
    private String department;
    private String lengthOfService;
    private String presentPostHeld;
    private String placeOfPosting;
}