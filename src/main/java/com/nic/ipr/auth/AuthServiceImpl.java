package com.nic.ipr.auth;

import com.nic.ipr.entity.User;
import com.nic.ipr.repository.UserRepository;
import com.nic.ipr.security.JwtUtil;
import com.nic.ipr.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));

        if (!request.getPassword().equals(user.getPassword())) {
            throw new BadRequestException("Invalid username or password");
        }

        Long employeeId = user.getEmployee() != null ? user.getEmployee().getId() : null;
        String name = user.getEmployee() != null ? user.getEmployee().getName() : "Authority";

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole(),
                user.getId(),
                employeeId
        );

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setUserId(user.getId());
        response.setEmployeeId(employeeId);
        response.setName(name);

        return response;
    }
}