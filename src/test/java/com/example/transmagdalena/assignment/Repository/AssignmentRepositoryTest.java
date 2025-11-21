package com.example.transmagdalena.assignment.Repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.assignment.repository.AssignmentRepository;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.TripStatus;
import com.example.transmagdalena.trip.repository.TripRepository;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Optional;

public class AssignmentRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripRepository tripRepository;

    @Test
    @DisplayName("buscar assignment por id")
    void buscarAssignmentPorId() {

        // Crear usuarios con campos obligatorios
        User driver = User.builder()
                .name("Juan Perez")
                .email("juan@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        User dispatcher = User.builder()
                .name("Maria Lopez")
                .email("maria@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        // Crear trip con campos obligatorios
        Trip trip = Trip.builder()
                .tripStatus(TripStatus.SCHEDULED)
                .date(LocalDate.now())
                .departureAt(LocalTime.now())
                .arrivalAt(LocalTime.now().plusHours(2))
                .build();

        driver = userRepository.save(driver);
        dispatcher = userRepository.save(dispatcher);
        trip = tripRepository.save(trip);

        Assignment assignment = Assignment.builder()
                .driver(driver)
                .dispatcher(dispatcher)
                .trip(trip)
                .checkList(true)
                .assignedAt(OffsetDateTime.now())
                .build();

        assignment = assignmentRepository.save(assignment);

        Optional<Assignment> optional = assignmentRepository.findById(assignment.getId());
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(assignment.getId(), optional.get().getId());
        Assertions.assertEquals(assignment.getDriver().getId(), optional.get().getDriver().getId());
        Assertions.assertEquals(assignment.getDispatcher().getId(), optional.get().getDispatcher().getId());
        Assertions.assertEquals(assignment.getTrip().getId(), optional.get().getTrip().getId());
        Assertions.assertEquals(assignment.getCheckList(), optional.get().getCheckList());
    }

    @Test
    @DisplayName("buscar assignment por id no existente")
    void buscarAssignmentPorIdNoExistente() {

        Optional<Assignment> optional = assignmentRepository.findById(999L);
        Assertions.assertFalse(optional.isPresent());
    }
}