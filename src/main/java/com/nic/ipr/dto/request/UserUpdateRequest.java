package com.nic.ipr.dto.request;

import com.nic.ipr.shared.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank(message = "Username cannot be empty")
    private String username;

    private String password;

    @NotNull(message = "Role is required")
    private Role role;
}
