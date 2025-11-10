package com.example.transmagdalena.routeStop.DTO;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

public class RouteStopDTO {
    // Create
    public record fareRuleCreateRequest(@NotNull BigDecimal basePrice, @NotNull boolean isDynamicPricing) {}
    public record routeStopCreateRequest(@NotNull int stopOrder,
                                         @NotNull Long originId, //manejs la bidireccionalidad con el list de routestops en Stop(service)
                                         @NotNull Long destinationId,//lo mismoa ca
                                         @NotNull Long routeId, fareRuleCreateRequest fareRule) implements Serializable {}


    // Update

    public record routeStopUpdateRequest(int stopOrder, Long originId, Long destinationId,
                                         Long routeId, fareRuleUpdateRequest fareRule) implements Serializable{}

    public record fareRuleUpdateRequest(Long routeId, BigDecimal basePrice,
                                        boolean isDynamicPricing) implements Serializable {}

    // Response
    public record stopDTO(Long id, String name, float lat, float lng) implements Serializable{}
    public record routeDTO(Long id, String code, stopDTO origin, stopDTO destination) implements Serializable{}
    public record fareRuleDTO(Long id, BigDecimal basePrice, boolean isDinamycPricing) implements Serializable{}
    public record routeStopResponse(Long id, int stopOrder, routeDTO route,stopDTO origin, stopDTO destination, fareRuleDTO fareRule) implements Serializable {}
}
