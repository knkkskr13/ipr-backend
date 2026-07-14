package com.nic.ipr.dto.response;

import com.nic.ipr.shared.enums.WorkflowAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IprWorkflowLogResponse {
    private Long id;
    private Long iprReturnId;
    private WorkflowAction action;
    private Long actionByUserId;
    private String actionByUserName;
    private String role;
    private String remarks;
    private LocalDateTime actionDate;
}