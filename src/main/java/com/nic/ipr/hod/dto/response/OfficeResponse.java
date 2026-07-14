package com.nic.ipr.hod.dto.response;

import lombok.Data;

@Data
public class OfficeResponse {
    private Long id;
    private String name;
    private Long departmentId;
    private String departmentName;
}