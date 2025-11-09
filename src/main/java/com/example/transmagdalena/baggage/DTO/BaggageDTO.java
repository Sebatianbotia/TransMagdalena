package com.example.transmagdalena.baggage.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

public class BaggageDTO {

    public record baggageCreateRequest(
            @NotNull
            Integer weight,
            @NotBlank
            String tagCode,
            @NotNull
            BigDecimal fee
    ) implements Serializable{}

    public record baggageUpdateRequest(
            Integer weight,
            String tagCode,
            BigDecimal fee
    ) implements Serializable{}

    public record baggageResponse(
            Long id,
            Integer weight,
            String tagCode,
            BigDecimal fee
    ) implements Serializable{}
}
