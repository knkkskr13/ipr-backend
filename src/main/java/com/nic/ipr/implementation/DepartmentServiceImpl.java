package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.DepartmentRequest;
import com.nic.ipr.dto.response.DepartmentResponse;
import com.nic.ipr.entity.Department;
import com.nic.ipr.repository.DepartmentRepository;
import com.nic.ipr.service.DepartmentService;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponse addDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new BadRequestException("Department with this name already exists");
        }
        Department department = new Department();
        department.setName(request.getName());
        return toResponse(departmentRepository.save(department));
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        department.setName(request.getName());
        return toResponse(departmentRepository.save(department));
    }

    @Override
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found");
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        return toResponse(departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found")));
    }

    private DepartmentResponse toResponse(Department d) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(d.getId());
        response.setName(d.getName());
        return response;
    }
}