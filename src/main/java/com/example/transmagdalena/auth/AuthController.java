package com.example.transmagdalena.auth;

import com.example.transmagdalena.user.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthDTO.longinResponse> login(@RequestBody AuthDTO.loginRequest request) {
        AuthDTO.longinResponse response = authService.getPassword(request);
        return ResponseEntity.ok(response);
    }

}
