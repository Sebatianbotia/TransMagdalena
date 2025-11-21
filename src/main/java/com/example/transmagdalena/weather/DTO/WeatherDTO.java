package com.example.transmagdalena.weather.DTO;

import com.example.transmagdalena.weather.WeatherType;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class WeatherDTO {

    public record weatherCreateRequest(
            @NotNull WeatherType weatherType, @NotNull Float discount, LocalDate date, LocalTime startTime,
            LocalTime endTime, Long cityId
    )implements Serializable {}

    public record weatherUpdateRequest(
             WeatherType weatherType, Float discount, LocalDate date, LocalTime startTime,
             LocalTime endTime, Long cityId
    ) implements Serializable {}

    public record weatherRespose(
            Long id, WeatherType weatherType, Float discount, LocalDate date, LocalTime startTime,
            LocalTime endTime, Long cityId
    ) implements Serializable {}
}
