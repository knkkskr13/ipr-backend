package com.nic.ipr.controller;

import com.nic.ipr.dto.request.PropertyRequest;
import com.nic.ipr.dto.response.PropertyResponse;
import com.nic.ipr.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PropertyResponse> getAllProperties() {
        return propertyService.getAllProperties();
    }

    @GetMapping("/get/{id}")
    public PropertyResponse getPropertyById(@PathVariable Long id) {
        return propertyService.getPropertyById(id);
    }

    @GetMapping("/get/ipr/{iprId}")
    public List<PropertyResponse> getPropertiesByIprId(@PathVariable Long iprId) {
        return propertyService.getPropertiesByIprId(iprId);
    }

    @PostMapping("/add")
    public PropertyResponse addProperty(@Valid @RequestBody PropertyRequest request) {
        return propertyService.addProperty(request);
    }

    @PutMapping("/update/{id}")
    public PropertyResponse updateProperty(@PathVariable Long id,
                                           @Valid @RequestBody PropertyRequest request) {
        return propertyService.updateProperty(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
    }
}