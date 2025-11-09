package com.example.transmagdalena.seat.DTO;

import com.example.transmagdalena.seat.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class SeatDTO {



    public record seatCreateRequest(
            @NotNull
            int number,
            @NotNull
            SeatType type,
            @NotNull
            Long busId
    ) implements Serializable {}

    public record seatUpdateRequest(
            int number,
            SeatType type,
            Long busId
    ) implements Serializable {}

    public record seatResponse(
            Long id,
            int number,
            SeatType type,
            busDto bus
    ) implements Serializable {}

    public record busDto(
        @NotBlank
        String plate,
        @NotBlank
        String status
    ) implements Serializable {}

}
