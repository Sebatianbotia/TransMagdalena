package com.example.transmagdalena.stop.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class StopDTO {

    public record stopCreateRequest(
            @NotBlank
            String name,
            @NotNull
            Long cityId,
            @NotNull
            float lat,
            @NotNull
            float lng
    ) implements Serializable {}

    public record stopUpdateRequest(
            @NotNull
            Long id,
            @NotBlank
            String name,
            @NotNull
            Long cityId,
            @NotNull
            float lat,
            @NotNull
            float lng

    ) implements Serializable{}

    public record stopResponse(

            String name,

            cityDto city,

            float lat,

            float lng
    ) implements Serializable{}

    public record cityDto(
            @NotBlank
            String name
    ) implements  Serializable{}


}
