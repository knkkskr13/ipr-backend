package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.OfficeRequest;
import com.nic.ipr.dto.response.OfficeResponse;
import com.nic.ipr.entity.Department;
import com.nic.ipr.entity.Office;
import com.nic.ipr.repository.DepartmentRepository;
import com.nic.ipr.repository.OfficeRepository;
import com.nic.ipr.service.OfficeService;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public OfficeResponse addOffice(OfficeRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Office office = new Office();
        office.setName(request.getName());
        office.setDepartment(department);
        return toResponse(officeRepository.save(office));
    }

    @Override
    public OfficeResponse updateOffice(Long id, OfficeRequest request) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Office not found"));
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        office.setName(request.getName());
        office.setDepartment(department);
        return toResponse(officeRepository.save(office));
    }

    @Override
    public void deleteOffice(Long id) {
        if (!officeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Office not found");
        }
        officeRepository.deleteById(id);
    }

    @Override
    public List<OfficeResponse> getAllOffices() {
        return officeRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public OfficeResponse getOfficeById(Long id) {
        return toResponse(officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Office not found")));
    }

    @Override
    public List<OfficeResponse> getOfficesByDepartmentId(Long departmentId) {
        return officeRepository.findByDepartmentId(departmentId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private OfficeResponse toResponse(Office o) {
        OfficeResponse response = new OfficeResponse();
        response.setId(o.getId());
        response.setName(o.getName());
        response.setDepartmentId(o.getDepartment().getId());
        response.setDepartmentName(o.getDepartment().getName());
        return response;
    }
}