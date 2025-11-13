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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.mapstruct.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user",  ignore = true)
    @Mapping(target = "trip",  ignore = true)
    @Mapping(target = "seatHold",  ignore = true)
    @Mapping(target = "origin",  ignore = true)
    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "qrCodeUrl", ignore = true)

    Ticket toEntity(TicketDTO.ticketCreateRequest ticketDTO);

    //UPDATE
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "trip",  ignore = true)
    @Mapping(target = "seatHold",  ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    @Mapping( target = "qrCodeUrl", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(TicketDTO.ticketUpdateRequest ticketUpdateRequest, @MappingTarget Ticket ticket);


    // RESPONSE
    TicketDTO.ticketResponse toDto(Ticket ticket);
    TicketDTO.userDTO toUserDTO(User user);

    default TicketDTO.stopDTO toStopDTO(Stop stop) {
        return new TicketDTO.stopDTO(stop.getId(), stop.getName(), stop.getCity().getName());
    }

    default TicketDTO.seatHoldDTO toSeatHoldDTO(SeatHold seatHold) {
        return new TicketDTO.seatHoldDTO(seatHold.getId(), seatHold.getStatus(),
                    seatHold.getSeat().getNumber(), seatHold.getSeat().getType()
                );
    }



}
