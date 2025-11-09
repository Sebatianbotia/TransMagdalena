package com.example.transmagdalena.user.Mapper;

import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO.userCreateRequest userDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UserDTO.userUpdateRequest userUpdateRequest, @MappingTarget User user);
    UserDTO.userResponse toResponse(User user);


}
