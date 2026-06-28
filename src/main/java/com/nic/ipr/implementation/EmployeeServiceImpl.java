package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.EmployeeRequest;
import com.nic.ipr.dto.response.EmployeeResponse;
import com.nic.ipr.entity.Employee;
import com.nic.ipr.exception.BadRequestException;
import com.nic.ipr.exception.ResourceNotFoundException;
import com.nic.ipr.repository.EmployeeRepository;
import com.nic.ipr.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    // ----------------------------------------------------------------
    // MAPPER METHODS
    // ----------------------------------------------------------------

    // RequestDTO → Entity  (used when saving to DB)
    private Employee mapToEntity(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setService(request.getService());
        employee.setDepartment(request.getDepartment());
        employee.setLengthOfService(request.getLengthOfService());
        employee.setPresentPostHeld(request.getPresentPostHeld());
        employee.setPlaceOfPosting(request.getPlaceOfPosting());
        return employee;
    }

    // Entity → ResponseDTO  (used when returning to controller)
    private EmployeeResponse mapToResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setEmail(employee.getEmail());
        response.setService(employee.getService());
        response.setDepartment(employee.getDepartment());
        response.setLengthOfService(employee.getLengthOfService());
        response.setPresentPostHeld(employee.getPresentPostHeld());
        response.setPlaceOfPosting(employee.getPlaceOfPosting());
        return response;
    }

    // ----------------------------------------------------------------
    // SERVICE METHODS
    // ----------------------------------------------------------------

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employee -> mapToResponse(employee))
                .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));

        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponse addEmployee(EmployeeRequest request) {

        // Check duplicate email
        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        Employee employee = mapToEntity(request);           // DTO → Entity
        Employee savedEmployee = employeeRepository.save(employee); // save to DB
        return mapToResponse(savedEmployee);                        // Entity → Response DTO
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));

        // Update fields from request
        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setService(request.getService());
        existing.setDepartment(request.getDepartment());
        existing.setLengthOfService(request.getLengthOfService());
        existing.setPresentPostHeld(request.getPresentPostHeld());
        existing.setPlaceOfPosting(request.getPlaceOfPosting());

        Employee savedEmployee = employeeRepository.save(existing);
        return mapToResponse(savedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));

        employeeRepository.delete(employee);
    }
}