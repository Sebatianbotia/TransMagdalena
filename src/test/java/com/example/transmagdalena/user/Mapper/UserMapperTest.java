package com.example.transmagdalena.user.Mapper;

import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToEntity() {
        UserDTO.userCreateRequest createRequest = new UserDTO.userCreateRequest(
                "Juan Perez",
                "juan@example.com",
                "3001234567",
                UserRols.CLERK,
                LocalDate.of(1990, 1, 1)
        );

        User user = userMapper.toEntity(createRequest);

        assertNotNull(user);
        assertEquals("Juan Perez", user.getName());
        assertEquals("juan@example.com", user.getEmail());
        assertEquals("3001234567", user.getPhone());
        assertEquals(UserRols.CLERK, user.getRol());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBornDate());
    }

    @Test
    void testUpdate() {
        User user = User.builder()
                .id(1L)
                .name("Maria Lopez")
                .email("maria@example.com")
                .phone("3109876543")
                .rol(UserRols.DRIVER)
                .bornDate(LocalDate.of(1985, 5, 15))
                .build();

        UserDTO.userUpdateRequest updateRequest = new UserDTO.userUpdateRequest(
                "Maria Garcia",
                "maria.garcia@example.com",
                "3111111111",
                UserRols.DRIVER
        );

        userMapper.update(updateRequest, user);

        assertEquals(1L, user.getId());
        assertEquals("Maria Garcia", user.getName());
        assertEquals("maria.garcia@example.com", user.getEmail());
        assertEquals("3111111111", user.getPhone());
        assertEquals(UserRols.DRIVER, user.getRol());
        assertEquals(LocalDate.of(1985, 5, 15), user.getBornDate()); // No debe cambiar
    }

    @Test
    void testUpdateWithNullValues() {
        User user = User.builder()
                .id(1L)
                .name("Carlos Ruiz")
                .email("carlos@example.com")
                .phone("3205556666")
                .rol(UserRols.CLERK)
                .bornDate(LocalDate.of(1992, 8, 20))
                .build();

        UserDTO.userUpdateRequest updateRequest = new UserDTO.userUpdateRequest(
                null, null, null, null
        );

        userMapper.update(updateRequest, user);

        // Los valores originales deben mantenerse por NullValuePropertyMappingStrategy.IGNORE
        assertEquals(1L, user.getId());
        assertEquals("Carlos Ruiz", user.getName());
        assertEquals("carlos@example.com", user.getEmail());
        assertEquals("3205556666", user.getPhone());
        assertEquals(UserRols.CLERK, user.getRol());
        assertEquals(LocalDate.of(1992, 8, 20), user.getBornDate());
    }

    @Test
    void testToResponse() {
        User user = User.builder()
                .id(1L)
                .name("Ana Torres")
                .email("ana@example.com")
                .phone("3154443333")
                .rol(UserRols.DRIVER)
                .bornDate(LocalDate.of(1988, 3, 10))
                .build();

        UserDTO.userResponse response = userMapper.toResponse(user);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Ana Torres", response.name());
        assertEquals("ana@example.com", response.email());
        assertEquals("3154443333", response.phone());
        assertEquals(UserRols.DRIVER, response.rol());
        assertEquals(LocalDate.of(1988, 3, 10), response.bornDate());
    }

    @Test
    void testToResponseAssignment() {
        User user = User.builder()
                .id(2L)
                .name("Pedro Martinez")
                .email("pedro@example.com")
                .phone("3178889999")
                .rol(UserRols.DRIVER)
                .build();

        UserDTO.userAssigmentResponse response = userMapper.toResponseAssigment(user);

        assertNotNull(response);
        assertEquals(2L, response.id());
        assertEquals("Pedro Martinez", response.name());
        assertEquals("pedro@example.com", response.email());
        assertEquals("3178889999", response.phone());
        assertEquals(UserRols.DRIVER, response.rol());
        assertNull(response.driverAssignments()); // No se han asignado assignments
        assertNull(response.dispatcherAssignments());
    }

    @Test
    void testAssignments() {
        Stop origin = Stop.builder().name("Barranquilla").build();
        Stop destination = Stop.builder().name("Cartagena").build();
        Route route = Route.builder().origin(origin).destination(destination).build();

        Trip trip = Trip.builder()
                .id(1L)
                .arrivalAt(LocalTime.of(12, 0))
                .departureAt(LocalTime.of(8, 0))
                .route(route)
                .build();

        Assignment assignment1 = Assignment.builder().id(1L).trip(trip).build();
        Assignment assignment2 = Assignment.builder().id(2L).trip(trip).build();

        Set<Assignment> assignments = new HashSet<>();
        assignments.add(assignment1);
        assignments.add(assignment2);

        Set<UserDTO.assignmentDTO> assignmentDTOs = userMapper.assignments(assignments);

        assertNotNull(assignmentDTOs);
        assertEquals(2, assignmentDTOs.size());

        // Verificar que se mapearon correctamente
        assignmentDTOs.forEach(assignmentDTO -> {
            assertNotNull(assignmentDTO.id());
            assertNotNull(assignmentDTO.trip());
            assertEquals(1L, assignmentDTO.trip().id());
            assertEquals(LocalTime.of(12, 0), assignmentDTO.trip().arrivalAt());
            assertEquals(LocalTime.of(8, 0), assignmentDTO.trip().departureAt());
        });
    }

    @Test
    void testTrip() {
        Stop origin = Stop.builder().name("Santa Marta").build();
        Stop destination = Stop.builder().name("Fundación").build();
        Route route = Route.builder().origin(origin).destination(destination).isDelete(false).build();

        Trip trip = Trip.builder()
                .id(3L)
                .arrivalAt(LocalTime.of(14, 0))
                .departureAt(LocalTime.of(10, 0))
                .route(route)
                .build();

        UserDTO.tripDTO tripDTO = userMapper.trip(trip);

        assertNotNull(tripDTO);
        assertEquals(3L, tripDTO.id());
        assertEquals(LocalTime.of(14, 0), tripDTO.arrivalAt());
        assertEquals(LocalTime.of(10, 0), tripDTO.departureAt());
        assertEquals("Santa Marta", tripDTO.origin());
        assertEquals("Fundación", tripDTO.destination());
    }

    @Test
    void testTripWithNullRoute() {
        Trip trip = Trip.builder()
                .id(4L)
                .arrivalAt(LocalTime.of(16, 0))
                .departureAt(LocalTime.of(12, 0))
                .route(null) // Route es null
                .build();

        UserDTO.tripDTO tripDTO = userMapper.trip(trip);

        assertNotNull(tripDTO);
        assertEquals(4L, tripDTO.id());
        assertEquals(LocalTime.of(16, 0), tripDTO.arrivalAt());
        assertEquals(LocalTime.of(12, 0), tripDTO.departureAt());
        assertEquals("", tripDTO.origin()); // Debe retornar string vacío por el método default
        assertEquals("", tripDTO.destination()); // Debe retornar string vacío por el método default
    }

    @Test
    void testOriginTrip() {
        Stop origin = Stop.builder().name("Sincelejo").build();
        Stop destination = Stop.builder().name("Montería").build();
        Route route = Route.builder().origin(origin).destination(destination).build();

        String originName = userMapper.originTrip(route);

        assertEquals("Sincelejo", originName);
    }

    @Test
    void testOriginTripWithNullRoute() {
        String originName = userMapper.originTrip(null);

        assertEquals("", originName);
    }

    @Test
    void testDestinationTrip() {
        Stop origin = Stop.builder().name("Valledupar").build();
        Stop destination = Stop.builder().name("Riohacha").build();
        Route route = Route.builder().origin(origin).destination(destination).build();

        String destinationName = userMapper.destinationTrip(route);

        assertEquals("Riohacha", destinationName);
    }

    @Test
    void testDestinationTripWithNullRoute() {
        String destinationName = userMapper.destinationTrip(null);

        assertEquals("", destinationName);
    }
}