package com.nic.ipr.implementation;

import com.nic.ipr.dto.response.IprWorkflowLogResponse;
import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.entity.IprWorkflowLog;
import com.nic.ipr.repository.IprWorkflowLogRepository;
import com.nic.ipr.service.IprWorkflowLogService;
import com.nic.ipr.shared.enums.WorkflowAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IprWorkflowLogServiceImpl implements IprWorkflowLogService {

    private final IprWorkflowLogRepository workflowLogRepository;
    private final com.nic.ipr.repository.UserRepository userRepository;

    @Override
    public void log(IprReturn iprReturn, WorkflowAction action, Long userId, String role, String remarks) {
        IprWorkflowLog log = new IprWorkflowLog();
        log.setIprReturn(iprReturn);
        log.setAction(action);
        log.setActionByUserId(userId);
        log.setRole(role);
        log.setRemarks(remarks);
        log.setActionDate(LocalDateTime.now());
        workflowLogRepository.save(log);
    }

    @Override
    public List<IprWorkflowLogResponse> getLogsByIprId(Long iprId) {
        return workflowLogRepository.findByIprReturnIprIdOrderByActionDateAsc(iprId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private IprWorkflowLogResponse toResponse(IprWorkflowLog log) {
        IprWorkflowLogResponse response = new IprWorkflowLogResponse();
        response.setId(log.getId());
        response.setIprReturnId(log.getIprReturn().getIprId());
        response.setAction(log.getAction());
        response.setActionByUserId(log.getActionByUserId());
        
        userRepository.findById(log.getActionByUserId()).ifPresent(user -> {
            if (user.getEmployee() != null) {
                response.setActionByUserName(user.getEmployee().getName());
            } else {
                response.setActionByUserName(user.getUsername());
            }
        });

        response.setRole(log.getRole());
        response.setRemarks(log.getRemarks());
        response.setActionDate(log.getActionDate());
        return response;
    }
}