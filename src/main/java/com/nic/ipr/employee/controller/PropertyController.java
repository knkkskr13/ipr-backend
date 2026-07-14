package com.nic.ipr.employee.controller;

import com.nic.ipr.employee.dto.request.PropertyRequest;
import com.nic.ipr.employee.dto.response.PropertyResponse;
import com.nic.ipr.employee.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee/property")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping("/get/ipr/{iprId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PropertyResponse>> getByIprId(@PathVariable Long iprId) {
        return ResponseEntity.ok(propertyService.getPropertiesByIprId(iprId));
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PropertyResponse> addProperty(@RequestBody PropertyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.addProperty(request));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PropertyResponse> updateProperty(@PathVariable Long id, @RequestBody PropertyRequest request) {
        return ResponseEntity.ok(propertyService.updateProperty(id, request));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }
}