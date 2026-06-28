package com.nic.ipr.controller;

import com.nic.ipr.dto.request.LoginRequest;
import com.nic.ipr.dto.request.RegisterRequest;
import com.nic.ipr.exception.BadRequestException;
import com.nic.ipr.security.JwtUtil;
import com.nic.ipr.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Map<String, String> register(@Valid @RequestBody RegisterRequest request) {


        userService.registerUser(
                request.getUsername(),
                request.getPassword(),
                request.getRole(),
                request.getEmployeeId()
        );
        return Map.of("message", "User registered successfully");
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest LoginCredentials) {
        boolean isValidLogin = userService.validateLogin(
                LoginCredentials.getUsername(),
                LoginCredentials.getPassword()
        );

        if (isValidLogin) {
            String username = LoginCredentials.getUsername();
            String role = userService.getUserRole(username);//because role isnt sent through Login credentials
            String token = jwtUtil.generateToken(username,role);
            return Map.of("token", token);
        } else {
            throw new BadRequestException("Invalid username or password");
        }
    }
}