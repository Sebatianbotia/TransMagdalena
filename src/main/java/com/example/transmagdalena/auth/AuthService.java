package com.example.transmagdalena.auth;

import com.example.transmagdalena.user.DTO.UserDTO;

public interface AuthService {
    AuthDTO.longinResponse getPassword(AuthDTO.loginRequest loginRequest);
}
