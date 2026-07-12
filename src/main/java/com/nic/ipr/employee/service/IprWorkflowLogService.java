package com.nic.ipr.employee.service;

import com.nic.ipr.employee.dto.response.IprWorkflowLogResponse;
import com.nic.ipr.employee.entity.IprReturn;
import com.nic.ipr.shared.status.WorkflowAction;

import java.util.List;

public interface IprWorkflowLogService {
    List<IprWorkflowLogResponse> getLogsByIprId(Long iprId);
    void logAction(IprReturn iprReturn, WorkflowAction action, Long actionByUserId, String role, String remarks);
}