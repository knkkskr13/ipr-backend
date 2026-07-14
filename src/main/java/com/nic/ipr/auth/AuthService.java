package com.nic.ipr.auth;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}