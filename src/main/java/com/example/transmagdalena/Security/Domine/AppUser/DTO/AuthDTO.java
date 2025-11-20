package com.example.transmagdalena.Security.Domine.AppUser.DTO;

import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.UserRols;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AuthDTO {

    public record RegisterRequest(@Email @NotBlank String email,
                                  @NotBlank String password, @NotBlank String name, @NotBlank String phone,
                                  @NotNull UserRols rol, @NotNull LocalDate bornDate) {
    }
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}

    public record AuthResponse(String accessToken, String tokenType, long expireInseconds, UserDTO.userResponse user) {}
}
