package com.nic.ipr.hod.implementation;

import com.nic.ipr.hod.dto.request.OfficeRequest;
import com.nic.ipr.hod.dto.response.OfficeResponse;
import com.nic.ipr.hod.entity.Department;
import com.nic.ipr.hod.entity.Office;
import com.nic.ipr.hod.repository.DepartmentRepository;
import com.nic.ipr.hod.repository.OfficeRepository;
import com.nic.ipr.hod.service.OfficeService;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    private final DepartmentRepository departmentRepository;

    private OfficeResponse mapToResponse(Office office) {
        OfficeResponse response = new OfficeResponse();
        response.setId(office.getId());
        response.setName(office.getName());
        response.setDepartmentId(office.getDepartment().getId());
        response.setDepartmentName(office.getDepartment().getName());
        return response;
    }

    @Override
    public List<OfficeResponse> getAllOffices() {
        return officeRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<OfficeResponse> getOfficesByDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department not found with id: " + departmentId);
        }
        return officeRepository.findByDepartmentId(departmentId).stream().map(this::mapToResponse).toList();
    }

    @Override
    public OfficeResponse getOfficeById(Long id) {
        return mapToResponse(officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Office not found with id: " + id)));
    }

    @Override
    public OfficeResponse addOffice(OfficeRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        if (officeRepository.existsByNameAndDepartmentId(request.getName(), request.getDepartmentId())) {
            throw new BadRequestException("Office '" + request.getName() + "' already exists in this department");
        }
        Office office = new Office();
        office.setName(request.getName());
        office.setDepartment(department);
        return mapToResponse(officeRepository.save(office));
    }

    @Override
    public OfficeResponse updateOffice(Long id, OfficeRequest request) {
        Office existing = officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Office not found with id: " + id));
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        existing.setName(request.getName());
        existing.setDepartment(department);
        return mapToResponse(officeRepository.save(existing));
    }

    @Override
    public void deleteOffice(Long id) {
        officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Office not found with id: " + id));
        officeRepository.deleteById(id);
    }
}