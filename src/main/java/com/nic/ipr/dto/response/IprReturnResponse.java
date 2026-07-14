package com.nic.ipr.dto.response;

import com.nic.ipr.shared.enums.IprStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IprReturnResponse {
    private Long iprId;
    private Long employeeId;
    private String employeeName;
    private String officeName;
    private String departmentName;
    private String reportingYear;
    private LocalDate asOnDate;
    private BigDecimal totalAnnualIncome;
    private Boolean isNoProperty;
    private IprStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
}