package com.example.transmagdalena.seat.DTO;

import com.example.transmagdalena.seat.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class SeatDTO {



    public record seatCreateRequest(
            @NotNull
            Integer number,
            @NotNull
            SeatType type,
            @NotNull
            Long busId
    ) implements Serializable {}

    public record seatUpdateRequest(
            Integer number,
            SeatType type,
            Long busId
    ) implements Serializable {}

    public record seatResponse(
            Long id,
            Integer number,
            SeatType type,
            busDto bus
    ) implements Serializable {}

    public record busDto(
        String plate,
        String status
    ) implements Serializable {}

}
