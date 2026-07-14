package com.nic.ipr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficeResponse {
    private Long id;
    private String name;
    private Long departmentId;
    private String departmentName;
}