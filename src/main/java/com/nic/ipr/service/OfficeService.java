package com.nic.ipr.service;

import com.nic.ipr.dto.request.OfficeRequest;
import com.nic.ipr.dto.response.OfficeResponse;
import java.util.List;

public interface OfficeService {
    OfficeResponse addOffice(OfficeRequest request);
    OfficeResponse updateOffice(Long id, OfficeRequest request);
    void deleteOffice(Long id);
    List<OfficeResponse> getAllOffices();
    OfficeResponse getOfficeById(Long id);
    List<OfficeResponse> getOfficesByDepartmentId(Long departmentId);
}