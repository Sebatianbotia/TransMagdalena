package com.example.transmagdalena.ticket.DTO;

import com.example.transmagdalena.seat.SeatType;
import com.example.transmagdalena.seatHold.SeatHoldStatus;
import com.example.transmagdalena.ticket.TicketPaymentMethod;
import com.example.transmagdalena.ticket.TicketStatus;
import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.TripStatus;
import com.example.transmagdalena.user.UserRols;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class TicketDTO {

public record ticketCreateRequest(@NotNull Long tripId, @NotNull Long seatHoldId, @NotNull Long userId, @NotNull String origin,
                                  @NotNull String destination, @NotNull BigDecimal price, @NotNull TicketStatus status,
                                  @NotNull TicketPaymentMethod paymentMethod, @NotNull String qrCodeUrl
)  implements Serializable {}


    public record ticketUpdateRequest( Long tripId, Long seatHoldId, Long userId,  String origin,
                                       String destination,  BigDecimal price,  TicketStatus status,
                                       TicketPaymentMethod paymentMethod, String qrCodeUrl
    )  implements Serializable {}

     // RESPONSE
    public record ticketResponse(Long id, tripDTO trip, seatHoldDTO seatHold, userDTO user, stopDTO origin,
                                 stopDTO destination, BigDecimal price, TicketStatus status,
                                 TicketPaymentMethod paymentMethod, String qrCodeUrl
                                 ) implements Serializable {}


    public record tripDTO(Long id , TripDTO.busDTO bus, OffsetDateTime date,
                          OffsetDateTime departureAt, OffsetDateTime arrivalAt,
                          TripStatus tripStatus) implements Serializable{}

    public record userDTO(
            String name,
            String email,
            String phone,
            UserRols rol
    ) implements Serializable {}


    public record stopDTO(Long id, String name, float lat, float lng) implements Serializable{}


    public record seatHoldDTO(
            Long id,
            SeatHoldStatus status,
            seatDto seat


    ) implements Serializable {}

    public record seatDto(
            int number,
            SeatType type
    ) implements Serializable {}
}
