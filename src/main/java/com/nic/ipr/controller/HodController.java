package com.nic.ipr.controller;

import com.nic.ipr.dto.request.IprNotificationRequest;
import com.nic.ipr.dto.request.IprNotificationUpdateRequest;
import com.nic.ipr.dto.request.IprReturnDecisionRequest;
import com.nic.ipr.dto.response.EmployeeResponse;
import com.nic.ipr.dto.response.IprNotificationResponse;
import com.nic.ipr.dto.response.IprReturnResponse;
import com.nic.ipr.dto.response.IprWorkflowLogResponse;
import com.nic.ipr.service.EmployeeService;
import com.nic.ipr.service.IprNotificationService;
import com.nic.ipr.service.IprReturnService;
import com.nic.ipr.service.IprWorkflowLogService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hod")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_HOD')")
public class HodController {

    private final IprReturnService iprReturnService;
    private final IprNotificationService notificationService;
    private final IprWorkflowLogService workflowLogService;
    private final EmployeeService employeeService;

    private Long getCurrentDepartmentId() {
        Claims claims = (Claims) SecurityContextHolder.getContext()
                .getAuthentication().getDetails();
        Long employeeId = claims.get("employeeId", Long.class);
        // departmentId resolved via employee in service layer
        return employeeId;
    }

    // -------------------- EMPLOYEES --------------------//

    @GetMapping("/employee/get")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesInDepartment() {
        Claims claims = (Claims) SecurityContextHolder.getContext()
                .getAuthentication().getDetails();
        Long employeeId = claims.get("employeeId", Long.class);
        // get department from employee
        EmployeeResponse hod = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employeeService.getEmployeesByDepartmentId(hod.getDepartmentId()));
    }

    // -------------------- IPR RETURNS --------------------//

    @GetMapping("/ipr-return/get")
    public ResponseEntity<List<IprReturnResponse>> getAllReturnsInDepartment() {
        return ResponseEntity.ok(iprReturnService.getAllIprReturnsForHodDepartment());
    }

    @GetMapping("/ipr-return/pending")
    public ResponseEntity<List<IprReturnResponse>> getPendingReturns() {
        return ResponseEntity.ok(iprReturnService.getSubmittedIprReturnsForHod());
    }

    @GetMapping("/ipr-return/get/{id}")
    public ResponseEntity<IprReturnResponse> getReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(iprReturnService.getIprReturnById(id));
    }

    @PutMapping("/ipr-return/approve/{id}")
    public ResponseEntity<IprReturnResponse> approveReturn(@PathVariable Long id,
                                                           @RequestBody(required = false) IprReturnDecisionRequest request) {
        String remarks = request != null ? request.getRemarks() : null;
        return ResponseEntity.ok(iprReturnService.approveIprReturn(id, remarks));
    }

    @PutMapping("/ipr-return/return/{id}")
    public ResponseEntity<IprReturnResponse> returnReturn(@PathVariable Long id,
                                                          @Valid @RequestBody IprReturnDecisionRequest request) {
        return ResponseEntity.ok(iprReturnService.returnIprReturn(id, request.getRemarks()));
    }

    // -------------------- NOTIFICATIONS --------------------//

    @PostMapping("/notification/add")
    public ResponseEntity<IprNotificationResponse> addNotification(@Valid @RequestBody IprNotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(request));
    }

    @PutMapping("/notification/update/{id}")
    public ResponseEntity<IprNotificationResponse> updateNotification(@PathVariable Long id,
                                                                      @Valid @RequestBody IprNotificationUpdateRequest request) {
        return ResponseEntity.ok(notificationService.updateNotification(id, request));
    }

    @DeleteMapping("/notification/delete/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/notification/get")
    public ResponseEntity<List<IprNotificationResponse>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/notification/get/office/{officeId}")
    public ResponseEntity<List<IprNotificationResponse>> getByOffice(@PathVariable Long officeId) {
        return ResponseEntity.ok(notificationService.getNotificationsByOffice(officeId));
    }

    @GetMapping("/notification/get/active/{officeId}")
    public ResponseEntity<IprNotificationResponse> getActiveByOffice(@PathVariable Long officeId) {
        return ResponseEntity.ok(notificationService.getActiveNotificationByOffice(officeId));
    }

    // -------------------- WORKFLOW LOG --------------------//

    @GetMapping("/workflow-log/get/{iprId}")
    public ResponseEntity<List<IprWorkflowLogResponse>> getWorkflowLog(@PathVariable Long iprId) {
        return ResponseEntity.ok(workflowLogService.getLogsByIprId(iprId));
    }
}