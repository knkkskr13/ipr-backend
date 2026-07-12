package com.nic.ipr.hod.controller;

import com.nic.ipr.hod.dto.request.OfficeRequest;
import com.nic.ipr.hod.dto.response.OfficeResponse;
import com.nic.ipr.hod.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/office")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('HOD')")
    public ResponseEntity<List<OfficeResponse>> getAllOffices() {
        return ResponseEntity.ok(officeService.getAllOffices());
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('HOD')")
    public ResponseEntity<OfficeResponse> getOfficeById(@PathVariable Long id) {
        return ResponseEntity.ok(officeService.getOfficeById(id));
    }

    @GetMapping("/get/department/{departmentId}")
    @PreAuthorize("hasRole('HOD')")
    public ResponseEntity<List<OfficeResponse>> getOfficesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(officeService.getOfficesByDepartment(departmentId));
    }

    @PostMapping("/add")
    @PreAuthorize("denyAll()")
    public ResponseEntity<OfficeResponse> addOffice(@RequestBody OfficeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(officeService.addOffice(request));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("denyAll()")
    public ResponseEntity<OfficeResponse> updateOffice(@PathVariable Long id, @RequestBody OfficeRequest request) {
        return ResponseEntity.ok(officeService.updateOffice(id, request));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("denyAll()")
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) {
        officeService.deleteOffice(id);
        return ResponseEntity.noContent().build();
    }
}