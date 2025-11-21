package com.example.transmagdalena.ticket.mapper;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seat.SeatType;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.ticket.DTO.TicketDTO;
import com.example.transmagdalena.ticket.Mapper.TicketMapper;
import com.example.transmagdalena.ticket.Ticket;
import com.example.transmagdalena.ticket.TicketPaymentMethod;
import com.example.transmagdalena.ticket.TicketStatus;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TicketMapperTest {
    private final TicketMapper ticketMapper = Mappers.getMapper(TicketMapper.class);

    @Test
    @DisplayName("DTO TO ENTIDAD")
    public void toEntity() {
        TicketDTO.ticketCreateRequest ticket = new TicketDTO.ticketCreateRequest(1L, 1L, 2L, 6L,
                8L, BigDecimal.valueOf(222), TicketStatus.SOLD, TicketPaymentMethod.CARD
                );


        var t = ticketMapper.toEntity(ticket);
        assertEquals(t.getStatus(), TicketStatus.SOLD);
        assertEquals(t.getPaymentMethod(), TicketPaymentMethod.CARD);
        assertEquals(t.getPrice(), BigDecimal.valueOf(222));
    }

    @Test
    @DisplayName("ENTIDAD TO DTO")
    public void toDto() {
        Trip trip = Trip.builder().id(1L).date(LocalDate.MAX).build();
        Seat seat = Seat.builder().id(1L).number(22).type(SeatType.PREFERENTIAL).build();
        SeatHold seatHold = SeatHold.builder().id(1L).seat(seat).build();
        User user = User.builder().id(1L).name("jose").rol(UserRols.CLERK).phone("2222").build();
        City city = City.builder().id(1L).name("Berlin").build();
        Stop origin = Stop.builder().id(1L).name("fundacin").lat(22F).lng(222F).city(city).build();
        Stop destination = Stop.builder().id(2L).name("santa marta").lat(223F).lng(2222F).city(city).build();


        Ticket ticket = Ticket.builder().id(1L).trip(trip).user(user).origin(origin)
                .destination(destination).seatHold(seatHold).paymentMethod(TicketPaymentMethod.CARD)
                .qrCodeUrl("hoggg").status(TicketStatus.SOLD).build();

        TicketDTO.ticketResponse ticketResponse = ticketMapper.toDto(ticket);

        assertEquals(ticketResponse.status(), ticket.getStatus());
        assertEquals(ticketResponse.paymentMethod(), ticket.getPaymentMethod());
        assertEquals(ticketResponse.qrCodeUrl(), ticket.getQrCodeUrl());
        assertEquals(ticketResponse.origin().id(), origin.getId());
        assertEquals(ticketResponse.seatHold().id(), seatHold.getId());
        assertEquals(ticketResponse.origin().name(), origin.getName());
        assertEquals(ticketResponse.seatHold().type(), seat.getType());
        assertEquals(ticketResponse.seatHold().seatNumber(), seat.getNumber());
        assertEquals(ticketResponse.destination().id(), destination.getId());
        assertEquals(ticketResponse.destination().name(), destination.getName());




    }


}
