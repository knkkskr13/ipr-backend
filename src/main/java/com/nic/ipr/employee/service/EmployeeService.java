package com.nic.ipr.employee.service;

import com.nic.ipr.employee.dto.response.EmployeeResponse;
import java.util.List;

public interface EmployeeService {
    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse getEmployeeById(Long id);

    // Added search method
    List<EmployeeResponse> searchEmployees(String name, String email, Long departmentId, Long officeId);
}