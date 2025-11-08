package com.example.transmagdalena.fareRule.DTO;

import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

public class FareRuleDTO {
    public record fareRuleCreateRequest(@NotNull Long routeId, @NotNull BigDecimal basePrice,
                                        @NotNull boolean isDynamicPricing) implements Serializable {}

    public record fareRuleResponse(Long id, RouteStopDTO.routeDTO route, BigDecimal basePrice,
                                   boolean isDynamicPricing
                                   ) implements Serializable {}
}
