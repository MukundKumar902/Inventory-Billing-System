package com.inventory.billing.service;

import com.inventory.billing.dto.LoginRequest;
import com.inventory.billing.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
