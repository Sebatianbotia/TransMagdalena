package com.example.transmagdalena.assignment.DTO;

import com.example.transmagdalena.user.UserRols;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class AssignmentDTO {
    public record assignmentCreateRequest(
            @NotNull
            Long tripId,
            @NotNull
            Long dispatcherId,
            @NotNull
            Long driverId,
            @NotNull
            Boolean checklist
    )implements Serializable {}


    public record assignmentUpdateRequest(
            @NotNull
            Long Id,
            @NotNull
            Long tripId,
            @NotNull
            Long driverId,
            @NotNull
            Long dispatcherId,
            @NotNull
            Boolean checklist
    ) implements Serializable {}

    public record assignmentResponse(
            Long id,
            userDTO driver,
            userDTO dispatcher,
            tripDTO trip
    ) implements Serializable {}

    public record userDTO(
            @Size(max = 30)
            String name,
            @NotNull
            @Size(max = 20)
            String email,
            @NotNull
            @Size(max = 13)
            String phone,
            @NotNull
            UserRols rol
            ) implements Serializable {}

    public record tripDTO(
            Long id,
            OffsetDateTime arrivalAt,
            OffsetDateTime departureAt
    ) implements Serializable {}

}

