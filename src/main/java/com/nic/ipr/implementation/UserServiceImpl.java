package com.nic.ipr.implementation;

import com.nic.ipr.dto.request.UserCredentialRequest;
import com.nic.ipr.dto.request.UserUpdateRequest;
import com.nic.ipr.dto.response.UserResponse;
import com.nic.ipr.entity.Employee;
import com.nic.ipr.entity.User;
import com.nic.ipr.repository.EmployeeRepository;
import com.nic.ipr.repository.UserRepository;
import com.nic.ipr.service.UserService;
import com.nic.ipr.shared.enums.Role;
import com.nic.ipr.shared.exception.BadRequestException;
import com.nic.ipr.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public UserResponse createCredentials(UserCredentialRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        if (request.getEmployeeId() != null) {
            if (userRepository.existsByEmployeeId(request.getEmployeeId())) {
                throw new BadRequestException("Credentials already exist for this employee");
            }
            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

            if (request.getRole() == Role.ROLE_HOD) {
                Long deptId = employee.getOffice().getDepartment().getId();
                if (userRepository.existsByRoleAndEmployeeOfficeDepartmentId(Role.ROLE_HOD, deptId)) {
                    throw new BadRequestException("A Head of Department already exists for this department");
                }
            }

            user.setEmployee(employee);
        }

        return toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateCredentials(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already taken");
        }

        if (request.getRole() == Role.ROLE_HOD && user.getEmployee() != null) {
            Long deptId = user.getEmployee().getOffice().getDepartment().getId();
            if (userRepository.existsByRoleAndEmployeeOfficeDepartmentIdAndIdNot(Role.ROLE_HOD, deptId, user.getId())) {
                throw new BadRequestException("A Head of Department already exists for this department");
            }
        }

        user.setUsername(request.getUsername());
        user.setRole(request.getRole());
        
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(request.getPassword());
        }

        return toResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getEmployee() != null) {
            user.getEmployee().setUser(null);
            user.setEmployee(null);
        }

        userRepository.delete(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        return toResponse(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    private UserResponse toResponse(User u) {
        UserResponse response = new UserResponse();
        response.setId(u.getId());
        response.setUsername(u.getUsername());
        response.setRole(u.getRole());
        response.setEmployeeId(u.getEmployee() != null ? u.getEmployee().getId() : null);
        response.setEmployeeName(u.getEmployee() != null ? u.getEmployee().getName() : null);
        return response;
    }
}