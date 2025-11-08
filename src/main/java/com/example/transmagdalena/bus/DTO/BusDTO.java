package com.example.transmagdalena.bus.DTO;

import com.example.transmagdalena.seat.SeatType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class BusDTO {
    public record busCreateRequest(
        @NotNull
        String plate,
        @NotNull
        int capacity,
        @NotBlank
        String status,
        @NotNull
        Set<seatCreateRequest> seats
    ) implements Serializable {}

    public record seatCreateRequest(
            @NotNull
            int number,
            @NotNull
            SeatType type
    ) implements Serializable {}

    public record busUpdateRequest(
            @NotNull
            Long id,
            @NotNull
            String plate,
            @NotNull
            int capacity,
            @NotBlank
            String status
    ) implements Serializable {}

    public record busResponse(
            Long id,
            String plate,
            int capacity,
            String status,
            Set<seatResponseDto> seats
    ) implements Serializable{}

    public record seatResponseDto(
            @NotNull
            Integer number,
            @NotBlank
            SeatType type
    ) implements Serializable{}
}
