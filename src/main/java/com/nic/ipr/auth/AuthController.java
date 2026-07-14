package com.nic.ipr.auth;

import com.nic.ipr.shared.exception.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// Update these imports to match exactly where your unified entities are stored!
import com.nic.ipr.employee.entity.User;
import com.nic.ipr.employee.repository.UserRepository;
import com.nic.ipr.security.JwtUtil;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Server-Authoritative Login Endpoint.
     * The React frontend ONLY sends: { "username": "...", "password": "..." }
     * The database dictates the role and token payload.
     */
    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginRequest request) {

        // 1. Find user by credentials (handles both HODs and Employees!)
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));

        // 2. Validate Password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid username or password");
        }

        // 3. Server dictates the role based on the database string ("HOD" or "EMPLOYEE")
        String userRole = user.getRole();

        // 4. Generate the single, unified token
        String token = jwtUtil.generateToken(user.getUsername(), userRole);

        // 5. Build the response payload
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", userRole); // Tell React where to redirect

        // If the user is an HOD, the frontend AuthContext expects a bit more data
        if ("HOD".equals(userRole)) {
            response.put("id", user.getId());

            // Safely fetch details from the attached Employee entity
            if (user.getEmployee() != null) {
                response.put("name", user.getEmployee().getName());

                Long departmentId = null;
                if (user.getEmployee().getDepartment() != null) {
                    departmentId = user.getEmployee().getDepartment().getId();
                } else if (user.getEmployee().getOffice() != null && user.getEmployee().getOffice().getDepartment() != null) {
                    departmentId = user.getEmployee().getOffice().getDepartment().getId();
                }
                if (departmentId != null) {
                    response.put("departmentId", departmentId);
                }
            }
        }

        return response;
    }
}