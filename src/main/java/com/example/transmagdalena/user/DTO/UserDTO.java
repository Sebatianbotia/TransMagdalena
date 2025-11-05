package com.example.transmagdalena.user.DTO;


import java.io.Serializable;
import java.time.OffsetDateTime;

public class UserDTO {
    public record userCreateRequest(String name, String email, String password, Long rolId)  implements Serializable {}
    public record userUpdateRequest(Long id, String name, String email, String password, Long rolId)  implements Serializable {}
    public record userResponse(Long id, String name, String email, String rol, OffsetDateTime createdAt, )
}
