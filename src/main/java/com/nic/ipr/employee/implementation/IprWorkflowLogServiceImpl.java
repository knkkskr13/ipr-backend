package com.nic.ipr.employee.implementation;

import com.nic.ipr.employee.dto.response.IprWorkflowLogResponse;
import com.nic.ipr.employee.entity.IprReturn;
import com.nic.ipr.employee.entity.IprWorkflowLog;
import com.nic.ipr.employee.entity.User;
import com.nic.ipr.employee.repository.IprReturnRepository;
import com.nic.ipr.employee.repository.IprWorkflowLogRepository;
import com.nic.ipr.employee.repository.UserRepository;
import com.nic.ipr.employee.service.IprWorkflowLogService;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import com.nic.ipr.shared.status.WorkflowAction;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IprWorkflowLogServiceImpl implements IprWorkflowLogService {

    private final IprWorkflowLogRepository workflowLogRepository;
    private final IprReturnRepository iprReturnRepository;
    private final UserRepository userRepository;

    // Helper method to get the logged-in user
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private IprWorkflowLogResponse mapToResponse(IprWorkflowLog log) {
        IprWorkflowLogResponse response = new IprWorkflowLogResponse();
        response.setId(log.getId());
        response.setIprReturnId(log.getIprReturn().getIprId());
        response.setAction(log.getAction());
        response.setActionByUserId(log.getActionByUserId());
        response.setRole(log.getRole());
        response.setRemarks(log.getRemarks());
        response.setActionDate(log.getActionDate());
        return response;
    }

    @Override
    public List<IprWorkflowLogResponse> getLogsByIprId(Long iprId) {
        // 1. Get the IPR Return from the database
        IprReturn iprReturn = iprReturnRepository.findById(iprId)
                .orElseThrow(() -> new ResourceNotFoundException("IPR Return not found"));

        User user = getCurrentUser();
        boolean isHod = user.getRole() != null && user.getRole().contains("HOD");

        // 2. Dynamic Security Check
        if (!isHod) {
            // Standard Employees can only view their own history
            if (!iprReturn.getEmployee().getId().equals(user.getEmployee().getId())) {
                throw new BadRequestException("Access denied: You cannot view this history.");
            }
        }

        // 3. Fetch and return the logs
        return workflowLogRepository.findByIprReturnIprId(iprId)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public void logAction(IprReturn iprReturn, WorkflowAction action, Long actionByUserId, String role, String remarks) {
        IprWorkflowLog log = new IprWorkflowLog();
        log.setIprReturn(iprReturn);
        log.setAction(action);
        log.setActionByUserId(actionByUserId);
        log.setRole(role);
        log.setRemarks(remarks);
        workflowLogRepository.save(log);
    }
}