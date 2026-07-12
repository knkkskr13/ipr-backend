package com.nic.ipr.employee.dto.response;

import com.nic.ipr.shared.status.WorkflowAction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IprWorkflowLogResponse {
    private Long id;
    private Long iprReturnId;
    private WorkflowAction action;
    private Long actionByUserId;
    private String role;
    private String remarks;
    private LocalDateTime actionDate;
}