package com.nic.ipr.controller;

import com.nic.ipr.dto.request.IprDeclarationRequest;
import com.nic.ipr.dto.request.IprReturnRequest;
import com.nic.ipr.dto.request.IprReturnUpdateRequest;
import com.nic.ipr.dto.request.PropertyRequest;
import com.nic.ipr.dto.response.*;
import com.nic.ipr.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_HOD')")
public class EmployeeController {

    private final IprReturnService iprReturnService;
    private final PropertyService propertyService;
    private final IprDeclarationService declarationService;
    private final IprWorkflowLogService workflowLogService;
    private final EmployeeService employeeService;
    private final IprNotificationService notificationService;

    // ─── ME ───────────────────────────────────────────────────────────────────

    @GetMapping("/me")
    public ResponseEntity<EmployeeResponse> getMe() {
        // employeeId is extracted inside the service via SecurityContext
        // We call getMyIprReturns() pattern — but for /me we need employeeId from token.
        // Use a dedicated service call that reads from SecurityContext internally.
        // For now, delegate to iprReturnService helper via a simple approach:
        // The service already reads from JWT claims — expose a getMe in EmployeeService.
        return ResponseEntity.ok(employeeService.getMe());
    }

    // ─── IPR RETURN ───────────────────────────────────────────────────────────

    @GetMapping("/ipr-return/get")
    public ResponseEntity<List<IprReturnResponse>> getMyReturns() {
        return ResponseEntity.ok(iprReturnService.getMyIprReturns());
    }

    @GetMapping("/ipr-return/get/{id}")
    public ResponseEntity<IprReturnResponse> getReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(iprReturnService.getIprReturnById(id));
    }

    @PostMapping("/ipr-return/add")
    public ResponseEntity<IprReturnResponse> addReturn(@Valid @RequestBody IprReturnRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iprReturnService.addIprReturn(request));
    }

    @PutMapping("/ipr-return/update/{id}")
    public ResponseEntity<IprReturnResponse> updateReturn(@PathVariable Long id,
                                                          @Valid @RequestBody IprReturnUpdateRequest request) {
        return ResponseEntity.ok(iprReturnService.updateIprReturn(id, request));
    }

    @PutMapping("/ipr-return/submit/{id}")
    public ResponseEntity<IprReturnResponse> submitReturn(@PathVariable Long id) {
        return ResponseEntity.ok(iprReturnService.submitIprReturn(id));
    }

    @DeleteMapping("/ipr-return/delete/{id}")
    public ResponseEntity<Void> deleteReturn(@PathVariable Long id) {
        iprReturnService.deleteIprReturn(id);
        return ResponseEntity.noContent().build();
    }

    // ─── PROPERTY ─────────────────────────────────────────────────────────────

    @PostMapping("/property/add")
    public ResponseEntity<PropertyResponse> addProperty(@Valid @RequestBody PropertyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.addProperty(request));
    }

    @PutMapping("/property/update/{id}")
    public ResponseEntity<PropertyResponse> updateProperty(@PathVariable Long id,
                                                           @Valid @RequestBody PropertyRequest request) {
        return ResponseEntity.ok(propertyService.updateProperty(id, request));
    }

    @DeleteMapping("/property/delete/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/property/get/{iprId}")
    public ResponseEntity<List<PropertyResponse>> getProperties(@PathVariable Long iprId) {
        return ResponseEntity.ok(propertyService.getPropertiesByIprId(iprId));
    }

    // ─── DECLARATION ──────────────────────────────────────────────────────────

    @PostMapping("/declaration/add")
    public ResponseEntity<IprDeclarationResponse> addDeclaration(@Valid @RequestBody IprDeclarationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(declarationService.addDeclaration(request));
    }

    @PutMapping("/declaration/update/{id}")
    public ResponseEntity<IprDeclarationResponse> updateDeclaration(@PathVariable Long id,
                                                                    @Valid @RequestBody IprDeclarationRequest request) {
        return ResponseEntity.ok(declarationService.updateDeclaration(id, request));
    }

    @DeleteMapping("/declaration/delete/{id}")
    public ResponseEntity<Void> deleteDeclaration(@PathVariable Long id) {
        declarationService.deleteDeclaration(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/declaration/get/{iprId}")
    public ResponseEntity<IprDeclarationResponse> getDeclaration(@PathVariable Long iprId) {
        return ResponseEntity.ok(declarationService.getDeclarationByIprId(iprId));
    }

    // ─── WORKFLOW LOG ─────────────────────────────────────────────────────────

    @GetMapping("/workflow-log/get/{iprId}")
    public ResponseEntity<List<IprWorkflowLogResponse>> getWorkflowLog(@PathVariable Long iprId) {
        return ResponseEntity.ok(workflowLogService.getLogsByIprId(iprId));
    }

    // ─── NOTIFICATION ─────────────────────────────────────────────────────────

    @GetMapping("/notification/get/active/{officeId}")
    public ResponseEntity<IprNotificationResponse> getActiveNotification(@PathVariable Long officeId) {
        return ResponseEntity.ok(notificationService.getActiveNotificationByOffice(officeId));
    }
}