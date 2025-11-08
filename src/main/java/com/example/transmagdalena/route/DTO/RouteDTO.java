package com.example.transmagdalena.route.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class RouteDTO {

    public record routeCreateRequest(
            @NotBlank
            String code,
            @NotNull
            Long originId,
            @NotNull
            Long destinationId

    ) implements Serializable {}

    public record routeUpdateRequest(
            @NotBlank
            String code,
            @NotNull
            Long originId,
            @NotNull
            Long destinationId
    ) implements Serializable {}

    public record routeResponse(
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
