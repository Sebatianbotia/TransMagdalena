package com.example.transmagdalena.assignment.Service;

import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.assignment.DTO.AssignmentDTO;
import com.example.transmagdalena.assignment.mapper.AssignmentMapper;
import com.example.transmagdalena.assignment.repository.AssignmentRepository;
import com.example.transmagdalena.assignment.service.AssignmentServiceImpl;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.service.TripService;
import com.example.transmagdalena.user.Service.UserService;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import com.example.transmagdalena.user.DTO.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceImplTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private AssignmentMapper assignmentMapper;

    @Mock
    private UserService userService;

    @Mock
    private TripService tripService;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    @Test
    void shouldSaveAssignmentSuccessfully() {
        // Given
        var createRequest = new AssignmentDTO.assignmentCreateRequest(
                1L, 2L, 3L, true, OffsetDateTime.now()
        );

        var assignment = new Assignment();
        var dispatcher = new User();
        var driver = new User();
        var trip = new Trip();
        dispatcher.setDispatcherAssignments(new HashSet<>());
        driver.setDriverAssignments(new HashSet<>());

        var userDTO = new UserDTO.userCreateRequest("John", "john@email.com", "123456789", UserRols.DRIVER, LocalDate.of(2025, 11,22));
        var tripDTO = new AssignmentDTO.tripDTO(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
        var expectedUserDTO = new AssignmentDTO.userDTO(userDTO.name(), userDTO.email(), userDTO.phone(), userDTO.rol());
        var expectedResponse = new AssignmentDTO.assignmentResponse(1L, expectedUserDTO, expectedUserDTO, tripDTO);

        when(assignmentMapper.toEntity(createRequest)).thenReturn(assignment);
        when(userService.getObject(2L, UserRols.DISPATCHER)).thenReturn(dispatcher);
        when(userService.getObject(3L, UserRols.DRIVER)).thenReturn(driver);
        when(tripService.getObject(1L)).thenReturn(trip);
        when(assignmentRepository.save(assignment)).thenReturn(assignment);
        when(assignmentMapper.toAssignmentDTO(assignment)).thenReturn(expectedResponse);

        // When
        var result = assignmentService.save(createRequest);

        // Then
        assertThat(result).isEqualTo(expectedResponse);
        verify(assignmentRepository).save(assignment);
    }

    @Test
    void shouldGetAssignmentById() {
        // Given
        Long assignmentId = 1L;
        var assignment = new Assignment();
        var userDTO = new UserDTO.userCreateRequest("John", "john@email.com", "123456789", UserRols.DRIVER, LocalDate.of(2025, 11,22));
        var tripDTO = new AssignmentDTO.tripDTO(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
        var expectedUserDTO = new AssignmentDTO.userDTO(userDTO.name(), userDTO.email(), userDTO.phone(), userDTO.rol());
        var expectedResponse = new AssignmentDTO.assignmentResponse(1L, expectedUserDTO, expectedUserDTO, tripDTO);

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(assignmentMapper.toAssignmentDTO(assignment)).thenReturn(expectedResponse);

        // When
        var result = assignmentService.get(assignmentId);

        // Then
        assertThat(result).isEqualTo(expectedResponse);
        verify(assignmentRepository).findById(assignmentId);
    }

    @Test
    void shouldThrowExceptionWhenAssignmentNotFound() {
        // Given
        Long assignmentId = 999L;
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> assignmentService.get(assignmentId));
        verify(assignmentRepository).findById(assignmentId);
    }

    @Test
    void shouldGetAllAssignmentsPaged() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        var assignment = new Assignment();
        var assignmentPage = new PageImpl<>(List.of(assignment));
        var userDTO = new UserDTO.userCreateRequest("John", "john@email.com", "123456789", UserRols.DRIVER, LocalDate.of(2025, 11,22));
        var expectedUserDTO = new AssignmentDTO.userDTO(userDTO.name(), userDTO.email(), userDTO.phone(), userDTO.rol());
        var tripDTO = new AssignmentDTO.tripDTO(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
        var expectedResponse = new AssignmentDTO.assignmentResponse(1L, expectedUserDTO, expectedUserDTO, tripDTO);

        when(assignmentRepository.findAll(pageable)).thenReturn(assignmentPage);
        when(assignmentMapper.toAssignmentDTO(assignment)).thenReturn(expectedResponse);

        // When
        Page<AssignmentDTO.assignmentResponse> result = assignmentService.getAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(expectedResponse);
        verify(assignmentRepository).findAll(pageable);
    }

    @Test
    void shouldDeleteAssignment() {
        // Given
        Long assignmentId = 1L;
        doNothing().when(assignmentRepository).deleteById(assignmentId);

        // When
        assignmentService.delete(assignmentId);

        // Then
        verify(assignmentRepository).deleteById(assignmentId);
    }

    @Test
    void shouldUpdateAssignmentPartially() {
        // Given
        Long assignmentId = 1L;
        var updateRequest = new AssignmentDTO.assignmentUpdateRequest(
                2L, 4L, null, true
        );

        var existingAssignment = new Assignment();
        var newDriver = new User();
        var newTrip = new Trip();

        var userDTO = new UserDTO.userCreateRequest("John", "john@email.com", "123456789", UserRols.DRIVER, LocalDate.of(2025, 11,22));
        var tripDTO = new AssignmentDTO.tripDTO(2L, LocalTime.of(14, 0), LocalTime.of(16, 0));
        var expectedUserDTO = new AssignmentDTO.userDTO(userDTO.name(), userDTO.email(), userDTO.phone(), userDTO.rol());
        var expectedResponse = new AssignmentDTO.assignmentResponse(1L, expectedUserDTO, expectedUserDTO, tripDTO);

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(existingAssignment));
        when(userService.getObject(4L, UserRols.DRIVER)).thenReturn(newDriver);
        when(tripService.getObject(2L)).thenReturn(newTrip);
        when(assignmentMapper.toAssignmentDTO(existingAssignment)).thenReturn(expectedResponse);

        // When
        var result = assignmentService.update(assignmentId, updateRequest);

        // Then
        assertThat(result).isEqualTo(expectedResponse);
        verify(assignmentMapper).updateEntity(updateRequest, existingAssignment);
        verify(userService).getObject(4L, UserRols.DRIVER);
        verify(tripService).getObject(2L);
        verify(userService, never()).getObject(any(), eq(UserRols.DISPATCHER));
    }

    @Test
    void shouldUpdateAssignmentWithAllFields() {
        // Given
        Long assignmentId = 1L;
        var updateRequest = new AssignmentDTO.assignmentUpdateRequest(
                2L, 4L, 5L, false
        );

        var existingAssignment = new Assignment();
        var newDriver = new User();
        var newDispatcher = new User();
        var newTrip = new Trip();

        var userDTO = new UserDTO.userCreateRequest("John", "john@email.com", "123456789", UserRols.DRIVER, LocalDate.of(2025, 11,22));
        var tripDTO = new AssignmentDTO.tripDTO(2L, LocalTime.of(9, 0), LocalTime.of(11, 0));
        var expectedUserDTO = new AssignmentDTO.userDTO(userDTO.name(), userDTO.email(), userDTO.phone(), userDTO.rol());
        var expectedResponse = new AssignmentDTO.assignmentResponse(1L, expectedUserDTO, expectedUserDTO, tripDTO);

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(existingAssignment));
        when(userService.getObject(4L, UserRols.DRIVER)).thenReturn(newDriver);
        when(userService.getObject(5L, UserRols.DISPATCHER)).thenReturn(newDispatcher);
        when(tripService.getObject(2L)).thenReturn(newTrip);
        when(assignmentMapper.toAssignmentDTO(existingAssignment)).thenReturn(expectedResponse);

        // When
        var result = assignmentService.update(assignmentId, updateRequest);

        // Then
        assertThat(result).isEqualTo(expectedResponse);
        verify(assignmentMapper).updateEntity(updateRequest, existingAssignment);
        verify(userService).getObject(4L, UserRols.DRIVER);
        verify(userService).getObject(5L, UserRols.DISPATCHER);
        verify(tripService).getObject(2L);
    }

    @Test
    void shouldGetObjectReturnAssignment() {
        // Given
        Long assignmentId = 1L;
        var assignment = new Assignment();
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        // When
        var result = assignmentService.getObject(assignmentId);

        // Then
        assertThat(result).isEqualTo(assignment);
        verify(assignmentRepository).findById(assignmentId);
    }

    @Test
    void shouldGetObjectThrowExceptionWhenNotFound() {
        // Given
        Long assignmentId = 999L;
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> assignmentService.getObject(assignmentId));
        verify(assignmentRepository).findById(assignmentId);
    }
}