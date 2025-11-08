package com.example.transmagdalena.assignment.mapper;


import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.assignment.DTO.AssignmentDTO;
import com.example.transmagdalena.assignment.DTO.AssignmentDTO.*;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper (componentModel = "spring")
public interface AssignmentMapper {
    //To entity
    //se ignoran los campos con id, se van a manejar en el service (opcion mas limpia)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "dispatcher", ignore = true)
    @Mapping(target = "trip", ignore = true)
    Assignment toEntity(assignmentCreateRequest createRequest);

    //Update
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "dispatcher", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "assignedAt", ignore = true)
    void updateEntity(assignmentUpdateRequest updateRequest, @MappingTarget Assignment assignment);


    //To DTO
    assignmentResponse toAssignmentDTO(Assignment entity);
    userDTO toUserDTO(User user);
    tripDTO toTripDTO(Trip trip);

}
