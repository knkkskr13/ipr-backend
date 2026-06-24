package com.nic.ipr.controller;

import com.nic.ipr.security.JwtUtil;
import com.nic.ipr.service.UserService;
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
    public Map<String, String> register(@RequestBody Map<String, String> request) {

        Long employeeId = null;

        if (request.get("employeeId") != null) {
            employeeId = Long.parseLong(request.get("employeeId"));// converts string coming from api request to Long
        }

        userService.registerUser(
                request.get("username"),
                request.get("password"),
                request.get("role"),
                employeeId
        );

        return Map.of("message", "User registered successfully");
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        boolean isValidLogin = userService.validateLogin(
                credentials.get("username"),
                credentials.get("password")
        );

        if (isValidLogin) {
            String token = jwtUtil.generateToken(credentials.get("username"));
            return Map.of("token", token);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}