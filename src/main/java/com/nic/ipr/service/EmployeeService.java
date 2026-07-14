package com.nic.ipr.service;

import com.nic.ipr.dto.request.EmployeeRequest;
import com.nic.ipr.dto.response.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse addEmployee(EmployeeRequest request);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    void deleteEmployee(Long id);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(Long id);
    List<EmployeeResponse> getEmployeesByDepartmentId(Long departmentId);
    EmployeeResponse getMe();  // reads employeeId from JWT claims
}