package com.example.transmagdalena.trip.DTO;

import com.example.transmagdalena.configuration.Configuration;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.trip.TripStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

public class TripDTO {

    public record tripCreateRequest(@NotNull Long busId, @NotNull Long routeId,
                                    @NotNull LocalDate date,
                                     @NotNull LocalTime departureAt,
                                     @NotNull LocalTime arrivalAt,
                                  @NotNull TripStatus tripStatus, @NotNull Long fareRuleId
    ) implements Serializable {}


    public record tripUpdateRequest(Long busId, Long routeId,
                                    LocalDate date, LocalTime departureAt, LocalTime arrivalAt,
                                     TripStatus tripStatus, Long fareRuleId) implements Serializable {}


    public record tripResponse(Long id, String origin, String destination,
                               LocalTime departTime,
                                LocalTime arriveTime,
                                LocalDate date,
                               BigDecimal price, TripStatus status,
                               String busPlate
                               ) implements Serializable {}

    public record tripResponseWithSeatAvailable(tripResponse trip, Integer seatAvailable) implements Serializable{}

}
