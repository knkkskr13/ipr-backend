package com.nic.ipr.employee.controller;

import com.nic.ipr.employee.dto.response.IprWorkflowLogResponse;
import com.nic.ipr.employee.service.IprWorkflowLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workflow-log") // <-- Unified Route! No /employee or /hod
@RequiredArgsConstructor
public class IprWorkflowLogController {

    private final IprWorkflowLogService iprWorkflowLogService;

    @GetMapping("/get/ipr/{iprId}")
    @PreAuthorize("isAuthenticated()") // <-- Open to both roles!
    public ResponseEntity<List<IprWorkflowLogResponse>> getLogsByIprId(@PathVariable Long iprId) {
        return ResponseEntity.ok(iprWorkflowLogService.getLogsByIprId(iprId));
    }
}