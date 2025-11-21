package com.example.transmagdalena.user.DTO;
import com.example.transmagdalena.user.UserRols;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Set;

public class UserDTO {

    public record userCreateRequest(@NotBlank String name, @NotBlank String email,
                                    @NotBlank String phone, @NotNull UserRols rol,
                                    @NotNull LocalDate bornDate) implements Serializable{}
    public record userUpdateRequest(String name, String email, String phone, UserRols rol) implements Serializable{}
    public record userResponse(Long id, String name, String email, String phone, UserRols rol, LocalDate bornDate) implements Serializable{}
    public record userAssigmentResponse(Long id, String name, String email, String phone, UserRols rol,
                                        Set<assignmentDTO> driverAssignments, Set<assignmentDTO> dispatcherAssignments
                                        ) implements Serializable{}

    public record assignmentDTO(
            Long id,
            tripDTO trip
    ) implements Serializable {}

    public record tripDTO(
            Long id,
            LocalTime arrivalAt,
            LocalTime departureAt,
            String origin,
            String destination
    ) implements Serializable {}


}
