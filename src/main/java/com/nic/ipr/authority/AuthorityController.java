package com.nic.ipr.authority;

import com.nic.ipr.hod.dto.request.DepartmentRequest;
import com.nic.ipr.hod.dto.response.DepartmentResponse;
import com.nic.ipr.hod.dto.response.HodResponse;
import com.nic.ipr.hod.entity.Department;
import com.nic.ipr.hod.entity.Office;
import com.nic.ipr.hod.service.DepartmentService;
import com.nic.ipr.hod.repository.DepartmentRepository;
import com.nic.ipr.hod.repository.OfficeRepository;

import com.nic.ipr.employee.entity.Employee;
import com.nic.ipr.employee.repository.EmployeeRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authority")
@RequiredArgsConstructor
public class AuthorityController {

    private final DepartmentRepository departmentRepository;
    private final OfficeRepository officeRepository;
    private final DepartmentService departmentService;
    private final EmployeeRepository employeeRepository;

    //----------------------------- Department Controller -----------------------------------//
    @PostMapping("/department/add")
    public ResponseEntity<DepartmentResponse> addDepartment(@RequestBody DepartmentRequest request) {
        Department department = new Department();
        department.setName(request.getName());
        Department saved = departmentRepository.save(department);

        DepartmentResponse response = new DepartmentResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/department/update/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(@PathVariable Long id, @RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, request));
    }

    @DeleteMapping("/department/delete/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    //----------------------------- Office Controller -----------------------------------//

    @PostMapping("/office/add")
    public ResponseEntity<OfficeResponse> addOffice(@RequestBody OfficeRequest officeRequest) {
        Department department = departmentRepository.findById(officeRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Office office = new Office();
        office.setName(officeRequest.getName());
        office.setDepartment(department);
        Office savedOffice = officeRepository.save(office);

        OfficeResponse response = new OfficeResponse();
        response.setId(savedOffice.getId());
        response.setName(savedOffice.getName());
        response.setDepartmentId(savedOffice.getDepartment().getId());
        response.setDepartmentName(savedOffice.getDepartment().getName());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/office/update/{id}")
    public ResponseEntity<OfficeResponse> updateOffice(@PathVariable Long id, @RequestBody OfficeRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Office not found"));
        office.setName(request.getName());
        office.setDepartment(department);
        Office saved = officeRepository.save(office);

        OfficeResponse response = new OfficeResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setDepartmentId(saved.getDepartment().getId());
        response.setDepartmentName(saved.getDepartment().getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/office/delete/{id}")
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) {
        officeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //----------------------------- Unified HOD Controller -----------------------------//

    @PostMapping("/hod/add")
    public ResponseEntity<HodResponse> addHod(@RequestBody HodRequest hodRequest) {
        Department department = departmentRepository.findById(hodRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // 1. Create the unified Employee record
        Employee employee = new Employee();
        employee.setName(hodRequest.getName());
        employee.setEmail(hodRequest.getEmail());
        employee.setDepartment(department);
        employee.setPresentPostHeld("Head of Department");
        Employee savedEmployee = employeeRepository.save(employee);

        HodResponse response = new HodResponse();
        response.setId(savedEmployee.getId());
        response.setName(savedEmployee.getName());
        response.setEmail(savedEmployee.getEmail());
        response.setDepartmentId(savedEmployee.getDepartment().getId());
        response.setDepartmentName(savedEmployee.getDepartment().getName());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/hod/update/{id}")
    public ResponseEntity<HodResponse> updateHod(@PathVariable Long id, @RequestBody HodRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HOD not found"));

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(department);
        Employee saved = employeeRepository.save(employee);

        HodResponse response = new HodResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setEmail(saved.getEmail());
        response.setDepartmentId(saved.getDepartment().getId());
        response.setDepartmentName(saved.getDepartment().getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/hod/delete/{id}")
    public ResponseEntity<Void> deleteHod(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DTOs for AuthorityController
    @Data
    public static class OfficeRequest {
        private String name;
        private Long departmentId;
    }

    @Data
    public static class HodRequest {
        private String name;
        private String email;
        private Long departmentId;
    }

    @Data
    public static class OfficeResponse {
        private Long id;
        private String name;
        private Long departmentId;
        private String departmentName;
    }
}