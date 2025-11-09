package com.example.transmagdalena.user.DTO;
import com.example.transmagdalena.user.UserRols;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
public class UserDTO {

    public record userCreateRequest(@NotBlank String name, @NotBlank String email,
                                    @NotBlank String phone, @NotBlank UserRols rol, @NotBlank String passwordHash) implements Serializable{}
    public record userUpdateRequest(String name, String email, String phone, UserRols rol, String passwordHash) implements Serializable{}
    public record userResponse(Long id, String name, String email, String phone, UserRols rol) implements Serializable{}
}
