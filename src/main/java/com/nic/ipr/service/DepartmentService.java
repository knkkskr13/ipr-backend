package com.nic.ipr.service;

import com.nic.ipr.dto.request.DepartmentRequest;
import com.nic.ipr.dto.response.DepartmentResponse;
import java.util.List;

public interface DepartmentService {
    DepartmentResponse addDepartment(DepartmentRequest request);
    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);
    void deleteDepartment(Long id);
    List<DepartmentResponse> getAllDepartments();
    DepartmentResponse getDepartmentById(Long id);
}