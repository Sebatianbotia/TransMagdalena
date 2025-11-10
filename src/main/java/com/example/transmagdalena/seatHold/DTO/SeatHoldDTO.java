package com.example.transmagdalena.seatHold.DTO;

import com.example.transmagdalena.route.DTO.RouteDTO;
import com.example.transmagdalena.seat.SeatType;
import com.example.transmagdalena.seatHold.SeatHoldStatus;
import com.example.transmagdalena.user.UserRols;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class SeatHoldDTO {


    public record seatHoldCreateRequest(
            @NotNull
            SeatHoldStatus status,
            @NotNull
            Long userId,
            @NotNull
            Long tripId,
            @NotNull
            Long seatId
    ) implements Serializable {}

    public record seatHoldUpdateRequest(
            SeatHoldStatus status,
            Long userId,
            Long tripId,
            Long seatId
    ) implements Serializable {}

    public record seatHoldResponse(
            Long id,
            SeatHoldStatus status,
            userDTO user,
            tripDTO trip,
            seatDto seat


    ) implements Serializable {}

    public record userDTO(
            String name,
            String email,
            String phone,
            UserRols rol
    ) implements Serializable {}

    public record routeDTO(
            Long id,
            String code,
            String origin,
            String destination
    ) implements Serializable {}

    public record tripDTO(
            Long id,
            routeDTO route,
            OffsetDateTime arrivalAt,
            OffsetDateTime departureAt
    ) implements Serializable {}

    public record seatDto(
            int number,
            SeatType type
    ) implements Serializable {}
}
