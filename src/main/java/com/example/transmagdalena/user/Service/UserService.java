package com.example.transmagdalena.user.Service;

import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO.userResponse save(UserDTO.userCreateRequest userCreateRequest);
    UserDTO.userResponse get(Long id);
    Page<UserDTO.userResponse> getAll(Pageable pageable);
    UserDTO.userAssigmentResponse getAssigments(Long id);
    boolean delete(Long id);
    UserDTO.userResponse update(UserDTO.userUpdateRequest userUpdateRequest, Long id);
    User getObject(Long id);
    Page<UserDTO.userResponse> getUsersByRol(UserRols rol, Pageable pageable);
    Integer countUsersByRol(UserRols rol);
    Page<UserDTO.userResponse> getPassengers(Pageable pageable);
    User getObject(Long id, UserRols rol);

    }