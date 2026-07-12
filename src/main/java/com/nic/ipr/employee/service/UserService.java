package com.nic.ipr.employee.service;

import com.nic.ipr.employee.entity.Employee;
import com.nic.ipr.employee.entity.User;
import com.nic.ipr.employee.repository.EmployeeRepository;
import com.nic.ipr.employee.repository.UserRepository;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String username, String password, Long employeeId) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username already exists");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BadRequestException("Employee not found"));

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("EMPLOYEE");
        user.setEmployee(employee);

        userRepository.save(user);
    }

    public boolean validateLogin(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    public String getUserRole(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getRole();
    }
}