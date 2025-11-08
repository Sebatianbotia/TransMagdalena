package com.example.transmagdalena.ticket.Mapper;

import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seatHold.DTO.SeatHoldDTO;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.seatHold.SeatHoldStatus;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.ticket.DTO.TicketDTO;
import com.example.transmagdalena.ticket.Ticket;
import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.TripStatus;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    Ticket toEntity(TicketDTO.ticketCreateRequest ticketDTO);

    //UPDATE
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    void update(TicketDTO.ticketUpdateRequest ticketUpdateRequest, @MappingTarget Ticket ticket);


    // RESPONSE
    TicketDTO.ticketResponse toDto(Ticket ticket);

    TicketDTO.stopDTO toStopDTO(Stop stop);

    TicketDTO.tripDTO toTripDTO(Trip trip);

    TicketDTO.userDTO toUserDTO(User user);

    TicketDTO.seatHoldDTO toSeatHoldDTO(SeatHold seat);

    TicketDTO.seatDto toSeatDto(Seat seat);

}
