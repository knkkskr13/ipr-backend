package com.nic.ipr.employee.implementation;

import com.nic.ipr.employee.dto.response.EmployeeResponse;
import com.nic.ipr.employee.entity.Employee;
import com.nic.ipr.employee.entity.User;
import com.nic.ipr.employee.repository.EmployeeRepository;
import com.nic.ipr.employee.repository.UserRepository;
import com.nic.ipr.employee.service.EmployeeService;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository; // check current user role

    private EmployeeResponse mapToResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setEmail(employee.getEmail());
        response.setService(employee.getService());

        // Safely check department
        if (employee.getDepartment() != null) {
            response.setDepartmentName(employee.getDepartment().getName());
        } else if (employee.getOffice() != null && employee.getOffice().getDepartment() != null) {
            response.setDepartmentName(employee.getOffice().getDepartment().getName());
        }

        response.setLengthOfService(employee.getLengthOfService());
        response.setPresentPostHeld(employee.getPresentPostHeld());
        response.setPlaceOfPosting(employee.getPlaceOfPosting());
        if (employee.getOffice() != null) {
            response.setOfficeId(employee.getOffice().getId());
            response.setOfficeName(employee.getOffice().getName());
        }
        return response;
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapToResponse(employee);
    }

    @Override
    public List<EmployeeResponse> searchEmployees(String name, String email, Long departmentId, Long officeId) {
        // 1. Get the currently logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        String username = auth.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Security Barrier: Lock the department ID if the user is an HOD
        if ("HOD".equals(currentUser.getRole())) {
            if (currentUser.getEmployee() == null) {
                throw new BadRequestException("HOD profile is missing employee information.");
            }
            departmentId = currentUser.getEmployee().getDepartment().getId();
        }

        // 3. Execute query and map to DTOs
        List<Employee> employees = employeeRepository.searchEmployees(name, email, departmentId, officeId);

        return employees.stream()
                .map(this::mapToResponse)
                .toList();
    }
}