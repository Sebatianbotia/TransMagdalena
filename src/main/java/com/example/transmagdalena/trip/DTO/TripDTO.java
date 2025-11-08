package com.example.transmagdalena.trip.DTO;

import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.trip.TripStatus;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class TripDTO {

    public record tripCreateRequest(@NotNull Long busId, @NotNull Long routeId, @NotNull OffsetDateTime date,
                                  @NotNull OffsetDateTime departureAt, @NotNull OffsetDateTime arrivalAt,
                                  @NotNull TripStatus status
    ) implements Serializable {}

    public record busDTO(Long id,
                             String plate,
                             int capacity,
                             String status) implements Serializable {}

    public record stopDTO(Long id, String name, float lat, float lng) implements Serializable{}
    public record routeDTO(Long id, String code, stopDTO origin, stopDTO destination) implements Serializable{}

    public record tripResponse( Long id ,busDTO bus, routeDTO route, OffsetDateTime date,
                                OffsetDateTime departureAt, OffsetDateTime arrivalAt,
                                TripStatus status) implements Serializable {}
}
