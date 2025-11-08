package com.example.transmagdalena.assignment.mapper;


import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.assignment.DTO.AssignmentDTO.*;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.User;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface AssignmentMapper {

    assignmentResponse toAssignmentDTO(Assignment entity);

    userDTO toUserDTO(User user);

    tripDTO toTripDTO(Trip trip);


}
