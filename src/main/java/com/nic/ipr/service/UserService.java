package com.nic.ipr.service;

import com.nic.ipr.dto.request.UserCredentialRequest;
import com.nic.ipr.dto.request.UserUpdateRequest;
import com.nic.ipr.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse createCredentials(UserCredentialRequest request);
    UserResponse updateCredentials(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
}