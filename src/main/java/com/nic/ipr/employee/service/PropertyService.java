package com.nic.ipr.employee.service;

import com.nic.ipr.employee.dto.request.PropertyRequest;
import com.nic.ipr.employee.dto.response.PropertyResponse;

import java.util.List;

public interface PropertyService {
    List<PropertyResponse> getPropertiesByIprId(Long iprId);
    PropertyResponse addProperty(PropertyRequest request);
    PropertyResponse updateProperty(Long id, PropertyRequest request);
    void deleteProperty(Long id);
}