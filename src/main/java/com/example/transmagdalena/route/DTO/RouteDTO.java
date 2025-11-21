package com.example.transmagdalena.route.DTO;

import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.RouteStop;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class RouteDTO {

    public record routeCreateRequest(
            @NotBlank
            String code,
            @NotNull
            Long originId,//manejar la bidireccionalidad con la lista de stops en el service
            @NotNull
            Long destinationId,//lo mismo acá
            @NotNull
            Float distanceKm,
            @NotNull
            Float durationTime
    ) implements Serializable {}


    public record routeUpdateRequest(
            String code,
            Long originId,
            Long destinationId,
            Float distanceKm,
            Float durationTime
    ) implements Serializable {}

    public record routeResponse(
            Long id,
            String code,
            String origin,
            String destination,
            String distanceKm,
            String durationTime
    ) implements Serializable {}

    public record routeResponseStops(
            routeResponse route,
            List<routeStopDTO> routeStops
    )implements Serializable {}

    public record routeStopDTO(Long id, Integer stopOrder, String origin,
                                    String destination, BigDecimal price, Boolean isDinamycPricing) implements Serializable {}


}
