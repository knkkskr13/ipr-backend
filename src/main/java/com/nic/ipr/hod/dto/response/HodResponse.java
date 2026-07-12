package com.nic.ipr.hod.dto.response;

import lombok.Data;

@Data
public class HodResponse {
    private Long id;
    private String name;
    private String email;
    private Long departmentId;
    private String departmentName;
}