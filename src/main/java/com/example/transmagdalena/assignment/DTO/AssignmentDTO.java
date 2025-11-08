package com.example.transmagdalena.assignment.DTO;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class AssignmentDTO {
    public record assignmentCreateRequest(
    @NotNull
    Long driverId,
    @NotNull
    Long dispatcherId,
    @NotNull
    Boolean checklist
    )implements Serializable {}


    public record assignmentUpdateRequest(
            @NotNull
            Long assignmentId,
            @NotNull
            Long driverId,
            @NotNull
            Long dispatcherId,
            @NotNull
            Boolean checklist
    ) implements Serializable {}


}

