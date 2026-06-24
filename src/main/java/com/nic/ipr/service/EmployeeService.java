package com.nic.ipr.service;

import com.nic.ipr.entity.Employee;
import com.nic.ipr.repository.EmployeeRepository;
import com.nic.ipr.exception.BadRequestException;
import com.nic.ipr.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public Employee addEmployee(Employee employee) {

        if (employee.getEmail() != null &&
                employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));

        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setService(updatedEmployee.getService());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setLengthOfService(updatedEmployee.getLengthOfService());
        existingEmployee.setPresentPostHeld(updatedEmployee.getPresentPostHeld());
        existingEmployee.setPlaceOfPosting(updatedEmployee.getPlaceOfPosting());
        existingEmployee.setEmail(updatedEmployee.getEmail());

        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));

        employeeRepository.delete(employee);
    }
}