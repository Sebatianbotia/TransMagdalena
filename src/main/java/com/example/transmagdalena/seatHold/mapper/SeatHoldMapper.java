package com.example.transmagdalena.seatHold.mapper;


import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seatHold.DTO.SeatHoldDTO.*;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SeatHoldMapper {

    @Mapping(source = "userId", target = "user", ignore = true)
    @Mapping(source = "tripId", target = "trip", ignore = true)//se maneja en el service
    @Mapping(source = "seatId", target = "seat", ignore = true)
    SeatHold toEntity(seatHoldCreateRequest dto);

    @Mapping(source = "userId", target = "user", ignore = true)
    @Mapping(source = "tripId", target = "trip", ignore = true)
    @Mapping(source = "seatId", target = "seat",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(seatHoldUpdateRequest request, @MappingTarget SeatHold entity);



    seatHoldResponse toDTO(SeatHold entity);

    userDTO toDTO(User user);
    tripDTO toDTO(Trip trip);
    seatDto toDTO(Seat seat);
    routeDTO toDTO(Route route);

    default String toDTO(Stop stop){
        return stop.getName();
    }


}


