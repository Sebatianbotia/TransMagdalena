package com.example.transmagdalena.user.Mapper;

import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO.userCreateRequest userDTO);
    void update(UserDTO.userUpdateRequest userUpdateRequest, @MappingTarget User user);
    UserDTO.userResponse toResponse(User user);


}
