package com.nic.ipr.employee.controller;

import com.nic.ipr.employee.dto.request.IprReturnRequest;
import com.nic.ipr.employee.dto.request.IprReturnUpdateRequest;
import com.nic.ipr.employee.dto.response.IprReturnResponse;
import com.nic.ipr.employee.service.IprReturnService;
import com.nic.ipr.hod.dto.request.IprReturnDecisionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ipr-return") // Unified Route!
@RequiredArgsConstructor
public class IprReturnController {

    private final IprReturnService iprReturnService;

    // ==========================================
    // EMPLOYEE ENDPOINTS (Personal Dashboard)
    // ==========================================

    @GetMapping("/get")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<IprReturnResponse>> getAllIprReturns() {
        return ResponseEntity.ok(iprReturnService.getAllIprReturns());
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IprReturnResponse> getIprReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(iprReturnService.getIprReturnById(id));
    }

    @GetMapping("/get/employee/{employeeId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<IprReturnResponse>> getByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(iprReturnService.getIprReturnsByEmployeeId(employeeId));
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IprReturnResponse> addIprReturn(@RequestBody IprReturnRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iprReturnService.addIprReturn(request));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IprReturnResponse> updateIprReturn(@PathVariable Long id, @RequestBody IprReturnUpdateRequest request) {
        return ResponseEntity.ok(iprReturnService.updateIprReturn(id, request));
    }

    @PutMapping("/submit/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IprReturnResponse> submitIprReturn(@PathVariable Long id) {
        return ResponseEntity.ok(iprReturnService.submitIprReturn(id));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteIprReturn(@PathVariable Long id) {
        iprReturnService.deleteIprReturn(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // HOD ENDPOINTS (Managerial Dashboard)
    // ==========================================

    @GetMapping("/get/department")
    public ResponseEntity<List<IprReturnResponse>> getAllForDepartment() {
        return ResponseEntity.ok(iprReturnService.getAllIprReturnsForHodDepartment());
    }

    @GetMapping("/get/pending")
    public ResponseEntity<List<IprReturnResponse>> getPending() {
        return ResponseEntity.ok(iprReturnService.getSubmittedIprReturnsForHod());
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<IprReturnResponse> approve(@PathVariable Long id,
                                                     @RequestBody(required = false) IprReturnDecisionRequest request) {
        String remarks = request != null ? request.getRemarks() : null;
        return ResponseEntity.ok(iprReturnService.approveIprReturn(id, remarks));
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<IprReturnResponse> returnFiling(@PathVariable Long id,
                                                          @RequestBody IprReturnDecisionRequest request) {
        return ResponseEntity.ok(iprReturnService.returnIprReturn(id, request.getRemarks()));
    }
}