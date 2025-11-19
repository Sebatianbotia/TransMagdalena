package com.example.transmagdalena.auth;

import com.example.transmagdalena.user.DTO.UserDTO;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class AuthDTO {

    public record longinResponse(
            boolean valid,
            String token,
            UserDTO.userResponse user
    ) implements Serializable {}

    public record loginRequest(@NotBlank String email, @NotBlank String password) implements Serializable{}
}
