package com.example.transmagdalena.user.Service;

import com.example.transmagdalena.user.DTO.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO.userResponse save(UserDTO.userCreateRequest userCreateRequest);
    UserDTO.userResponse get(Long id);
    List<UserDTO.userResponse> getAll();
}
