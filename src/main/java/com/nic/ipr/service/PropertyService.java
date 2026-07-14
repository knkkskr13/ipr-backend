package com.nic.ipr.service;

import com.nic.ipr.dto.request.PropertyRequest;
import com.nic.ipr.dto.response.PropertyResponse;
import java.util.List;

public interface PropertyService {
    PropertyResponse addProperty(PropertyRequest request);
    PropertyResponse updateProperty(Long id, PropertyRequest request);
    void deleteProperty(Long id);
    List<PropertyResponse> getPropertiesByIprId(Long iprId);
}