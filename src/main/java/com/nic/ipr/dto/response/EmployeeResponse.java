package com.nic.ipr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String name;
    private String email;
    private String service;
    private String lengthOfService;
    private String presentPostHeld;
    private String placeOfPosting;
    private Long officeId;
    private String officeName;
    private Long departmentId;
    private String departmentName;
}