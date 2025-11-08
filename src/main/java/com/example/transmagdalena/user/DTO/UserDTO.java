package com.example.transmagdalena.user.DTO;
import com.example.transmagdalena.user.UserRols;

import java.io.Serializable;
public class UserDTO {

    public record userCreateRequest(String name, String email, String phone, UserRols rol, String passwordHash) implements Serializable{}
    public record userUpdateRequest(String name, String email, String phone, UserRols rol, String passwordHash) implements Serializable{}
    public record userResponse(Long id, String name, String email, String phone, UserRols rol) implements Serializable{}
}
