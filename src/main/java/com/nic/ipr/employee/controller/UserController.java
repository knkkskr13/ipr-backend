package com.nic.ipr.employee.controller;

import com.nic.ipr.employee.dto.response.EmployeeMeResponse;
import com.nic.ipr.employee.entity.Employee;
import com.nic.ipr.employee.entity.User;
import com.nic.ipr.employee.repository.UserRepository;
import com.nic.ipr.hod.entity.Office;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // 1. Main Endpoint
    @GetMapping("/get/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> getMe(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Employee employee = user.getEmployee();

        if ("HOD".equals(user.getRole())) {
            return ResponseEntity.ok(buildHodResponse(user, employee));
        }

        return ResponseEntity.ok(buildEmployeeResponse(user, employee));
    }

    // 2. Extracted Helper Method for HOD
    private Map<String, Object> buildHodResponse(User user, Employee employee) {
        Map<String, Object> hodResponse = new HashMap<>();

        // 1. Core User Fields
        hodResponse.put("userId", user.getId());
        hodResponse.put("username", user.getUsername());
        hodResponse.put("role", "HOD");

        if (employee != null) {
            // 2. Admin Fields (For the HOD Portal)
            hodResponse.put("hodId", employee.getId());

            // 3. Personal Fields (for "No Office Assigned" dashboard crash)
            hodResponse.put("employeeId", employee.getId());
            hodResponse.put("name", employee.getName());
            hodResponse.put("email", employee.getEmail());
            hodResponse.put("service", employee.getService());
            hodResponse.put("lengthOfService", employee.getLengthOfService());
            hodResponse.put("presentPostHeld", employee.getPresentPostHeld());
            hodResponse.put("placeOfPosting", employee.getPlaceOfPosting());

            // 4. Safe Office & Department Mapping
            Office office = employee.getOffice();
            if (office != null) {
                hodResponse.put("officeId", office.getId());
                hodResponse.put("officeName", office.getName());
                if (office.getDepartment() != null) {
                    hodResponse.put("departmentId", office.getDepartment().getId());
                    hodResponse.put("departmentName", office.getDepartment().getName());
                    hodResponse.put("department", office.getDepartment().getName()); // React fallback
                }
            } else if (employee.getDepartment() != null) {
                // If HOD is linked directly to Dept without an office
                hodResponse.put("departmentId", employee.getDepartment().getId());
                hodResponse.put("departmentName", employee.getDepartment().getName());
                hodResponse.put("department", employee.getDepartment().getName()); // React fallback
            }
        }
        return hodResponse;
    }
    // 3. Extracted Helper Method for Employee
    private EmployeeMeResponse buildEmployeeResponse(User user, Employee employee) {
        EmployeeMeResponse response = new EmployeeMeResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());

        if (employee != null) {
            response.setEmployeeId(employee.getId());
            response.setName(employee.getName());
            response.setEmail(employee.getEmail());
            response.setService(employee.getService());
            response.setLengthOfService(employee.getLengthOfService());
            response.setPresentPostHeld(employee.getPresentPostHeld());
            response.setPlaceOfPosting(employee.getPlaceOfPosting());

            Office office = employee.getOffice();
            if (office != null) {
                response.setOfficeId(office.getId());
                response.setOfficeName(office.getName());
                if (office.getDepartment() != null) {
                    response.setDepartment(office.getDepartment().getName());
                }
            }
        }
        return response;
    }
}