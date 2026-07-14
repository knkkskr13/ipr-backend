package com.nic.ipr.hod.dto.response;

import lombok.Data;

@Data
public class HodMeResponse {
    private Long hodId;
    private String username;
    private String name;
    private String email;
    private String role;
    private Long departmentId;
    private String departmentName;
}