package com.example.transmagdalena.fareRule.DTO;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

public class FareRuleDTO {
    public record fareRuleCreateRequest(@NotNull Long originId, @NotNull Long destinationId, @NotNull BigDecimal basePrice,
                                        @NotNull Boolean isDynamicPricing) implements Serializable {}


    public record stopDTO(Long id, String name, Float lat, Float lng) implements Serializable{}

    public record fareRuleResponse(Long id, stopDTO origin, stopDTO destination,
                                   BigDecimal basePrice,
                                   Boolean isDynamicPricing
                                   ) implements Serializable {}

    public record fareRuleUpdateRequest(Long originId, Long destinationId, BigDecimal basePrice,
                                        Boolean isDynamicPricing) implements Serializable {}
}
