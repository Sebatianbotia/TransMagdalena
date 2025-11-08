package com.example.transmagdalena.seatHold.mapper;


import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seatHold.DTO.SeatHoldDTO.*;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SeatHoldMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "tripId", ignore = true)//se maneja en el service
    @Mapping(target = "seatId", ignore = true)
    SeatHold toEntity(seatHoldCreateRequest dto);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "tripId", ignore = true)
    @Mapping(target = "seatId", ignore = true)
    void updateEntity(seatHoldUpdateRequest request, @MappingTarget SeatHold entity);


    seatHoldResponse toDTO(SeatHold entity);

    userDTO toDTO(User user);
    tripDTO toDTO(Trip trip);
    seatDto toDTO(Seat seat);


}


