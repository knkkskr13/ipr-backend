package com.nic.ipr.hod.implementation;

import com.nic.ipr.hod.dto.request.DepartmentRequest;
import com.nic.ipr.hod.dto.response.DepartmentResponse;
import com.nic.ipr.hod.entity.Department;
import com.nic.ipr.hod.repository.DepartmentRepository;
import com.nic.ipr.hod.service.DepartmentService;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private DepartmentResponse mapToResponse(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(department.getId());
        response.setName(department.getName());
        return response;
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        return mapToResponse(departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id)));
    }

    @Override
    public DepartmentResponse addDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new BadRequestException("Department already exists: " + request.getName());
        }
        Department dept = new Department();
        dept.setName(request.getName());
        return mapToResponse(departmentRepository.save(dept));
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        existing.setName(request.getName());
        return mapToResponse(departmentRepository.save(existing));
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.deleteById(id);
    }
}