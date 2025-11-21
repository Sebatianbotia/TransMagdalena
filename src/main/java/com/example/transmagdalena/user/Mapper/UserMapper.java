package com.example.transmagdalena.user.Mapper;

import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.User;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO.userCreateRequest userDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UserDTO.userUpdateRequest userUpdateRequest, @MappingTarget User user);

    UserDTO.userResponse toResponse(User user);

    UserDTO.userAssigmentResponse toResponseAssigment(User user);
    Set<UserDTO.assignmentDTO> assignments(Set<Assignment> assignments);

    @Mapping(target = "origin", source = "route", qualifiedByName = "originTrip")
    @Mapping(target = "destination", source = "route", qualifiedByName = "destinationTrip")
    UserDTO.tripDTO trip(Trip trips);

    @Named("originTrip") // se usa para nombrar esos metodos por default y se llamo arriba
    default String originTrip(Route route) {
        String origin = "";
        if (route != null){origin = route.getOrigin().getName();}
        return origin;
    }

    @Named("destinationTrip")
    default String destinationTrip(Route route) {
        String destination = "";
        if (route != null){destination = route.getDestination().getName();}
        return destination;
    }

}
