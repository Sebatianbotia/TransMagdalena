package com.example.transmagdalena.route.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class RouteDTO {

    public record routeCreateRequest(
            @NotBlank
            String code,
            @NotNull
            Long originId,//manejar la bidireccionalidad con la lista de stops en el service
            @NotNull
            Long destinationId//lo mismo acá

    ) implements Serializable {}

    public record routeUpdateRequest(
            @NotNull
            Long id,
            String code,
            Long originId,
            Long destinationId
    ) implements Serializable {}

    public record routeResponse(
            Long id,
            String code,
            stopDto origin,
            stopDto destination
    ) implements Serializable {}


    public record stopDto(
            @NotBlank
            String name,
            @NotNull
            float lat,
            @NotNull
            float lng
    ) implements Serializable {}
}
