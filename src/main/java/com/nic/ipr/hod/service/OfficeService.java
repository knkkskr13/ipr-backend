package com.nic.ipr.hod.service;

import com.nic.ipr.hod.dto.request.OfficeRequest;
import com.nic.ipr.hod.dto.response.OfficeResponse;

import java.util.List;

public interface OfficeService {
    List<OfficeResponse> getAllOffices();
    List<OfficeResponse> getOfficesByDepartment(Long departmentId);
    OfficeResponse getOfficeById(Long id);
    OfficeResponse addOffice(OfficeRequest request);
    OfficeResponse updateOffice(Long id, OfficeRequest request);
    void deleteOffice(Long id);
}