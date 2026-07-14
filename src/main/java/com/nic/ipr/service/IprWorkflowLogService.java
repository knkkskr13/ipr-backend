package com.nic.ipr.service;

import com.nic.ipr.dto.response.IprWorkflowLogResponse;
import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.shared.enums.WorkflowAction;
import java.util.List;

public interface IprWorkflowLogService {
    void log(IprReturn iprReturn, WorkflowAction action, Long userId, String role, String remarks);
    List<IprWorkflowLogResponse> getLogsByIprId(Long iprId);
}