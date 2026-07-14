package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.EmployeeRequest;
import com.nic.ipr.dto.response.EmployeeResponse;
import com.nic.ipr.entity.Employee;
import com.nic.ipr.entity.Office;
import com.nic.ipr.repository.EmployeeRepository;
import com.nic.ipr.repository.OfficeRepository;
import com.nic.ipr.service.EmployeeService;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final OfficeRepository officeRepository;

    @Override
    public EmployeeResponse getMe() {
        Claims claims = (Claims) SecurityContextHolder.getContext()
                .getAuthentication().getDetails();
        Long employeeId = claims.get("employeeId", Long.class);
        if (employeeId == null) {
            throw new BadRequestException("No employee linked to this account");
        }
        return getEmployeeById(employeeId);
    }

    @Override
    public EmployeeResponse addEmployee(EmployeeRequest request) {
        Office office = officeRepository.findById(request.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found"));
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setService(request.getService());
        employee.setLengthOfService(request.getLengthOfService());
        employee.setPresentPostHeld(request.getPresentPostHeld());
        employee.setPlaceOfPosting(request.getPlaceOfPosting());
        employee.setOffice(office);
        return toResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        Office office = officeRepository.findById(request.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found"));
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setService(request.getService());
        employee.setLengthOfService(request.getLengthOfService());
        employee.setPresentPostHeld(request.getPresentPostHeld());
        employee.setPlaceOfPosting(request.getPlaceOfPosting());
        employee.setOffice(office);
        return toResponse(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        return toResponse(employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found")));
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findByOfficeDepartmentId(departmentId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private EmployeeResponse toResponse(Employee e) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(e.getId());
        response.setName(e.getName());
        response.setEmail(e.getEmail());
        response.setService(e.getService());
        response.setLengthOfService(e.getLengthOfService());
        response.setPresentPostHeld(e.getPresentPostHeld());
        response.setPlaceOfPosting(e.getPlaceOfPosting());
        response.setOfficeId(e.getOffice().getId());
        response.setOfficeName(e.getOffice().getName());
        response.setDepartmentId(e.getOffice().getDepartment().getId());
        response.setDepartmentName(e.getOffice().getDepartment().getName());
        return response;
    }
}