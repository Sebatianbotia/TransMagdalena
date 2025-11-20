package com.example.transmagdalena.trip.DTO;

import com.example.transmagdalena.configuration.Configuration;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.trip.TripStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class TripDTO {

    public record tripCreateRequest(@NotNull Long busId, @NotNull Long routeId, @NotNull OffsetDateTime date,
                                  @NotNull OffsetDateTime departureAt, @NotNull OffsetDateTime arrivalAt,
                                  @NotNull TripStatus tripStatus, @NotNull Long fareRuleId
    ) implements Serializable {}


    public record tripUpdateRequest(Long busId, Long routeId,  OffsetDateTime date,
                                     OffsetDateTime departureAt,  OffsetDateTime arrivalAt,
                                     TripStatus tripStatus, Long fareRuleId) implements Serializable {}


    public record tripResponse(Long id, String origin, String destination,
                               @JsonFormat(pattern = Configuration.HOUR_FORMAT) OffsetDateTime departTime,
                               @JsonFormat(pattern = Configuration.HOUR_FORMAT) OffsetDateTime arriveTime,
                               @JsonFormat(pattern = Configuration.DATE_FORMAT) OffsetDateTime date,
                               BigDecimal price, TripStatus status,
                               String busPlate
                               ) implements Serializable {}

    public record tripResponseWithSeatAvailable(tripResponse trip, Integer seatAvailable) implements Serializable{}

}
