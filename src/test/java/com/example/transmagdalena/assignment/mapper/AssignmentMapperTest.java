package com.example.transmagdalena.assignment.mapper;

import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.assignment.DTO.AssignmentDTO.*    ;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentMapperTest {

    private final AssignmentMapper Mapper = Mappers.getMapper(AssignmentMapper.class);

    @Test
    void toEntity() {
        assignmentCreateRequest CreateRequest = new assignmentCreateRequest(12L, 13L, 14L, true, OffsetDateTime.now());
        Assignment assignment = Mapper.toEntity(CreateRequest);
        assertNotNull(assignment);
        assertTrue(assignment.isCheckList());

    }
    @Test

    void toDTO() {
        User driver = User.builder().id(1L).email("aaa123@gmnail.com").name("alberto ramos").phone("323232323").rol(UserRols.DRIVER).build();
        User dispatcher = User.builder().id(2L).email("bbb123@gmnail.com").name("sergioo ramos").phone("323232323").rol(UserRols.DISPATCHER).build();

        Trip trip = Trip.builder().id(1L).departureAt(OffsetDateTime.now()).arrivalAt(OffsetDateTime.now().plusHours(12)).build();

        Assignment ass = Assignment.builder().id(1L).driver(driver).dispatcher(dispatcher).trip(trip).build();

        assignmentResponse res = Mapper.toAssignmentDTO(ass);

        assertNotNull(res);
        assertEquals(ass.getDispatcher().getName(), dispatcher.getName());

    }

    @Test
    void updateEntity() {
        User oldDriver = User.builder().id(1L).email("aaa123@gmnail.com").name("alberto ramos").phone("323232323").rol(UserRols.DRIVER).build();
        User dispatcher = User.builder().id(2L).email("bbb123@gmnail.com").name("sergioo ramos").phone("323232323").rol(UserRols.DISPATCHER).build();
        User newDriver = User.builder().id(4L).email("aaa123@gmnail.com").name("pipe cohen").phone("112233").rol(UserRols.DRIVER).build();

        Trip trip = Trip.builder().id(6L).departureAt(OffsetDateTime.now()).arrivalAt(OffsetDateTime.now().plusHours(12)).build();

        Assignment ass = Assignment.builder().id(8L).driver(oldDriver).dispatcher(dispatcher).checkList(false).trip(trip).build();
        var changes = new assignmentUpdateRequest(8L, 6L, 1L, 2L, true);

        Mapper.updateEntity(changes, ass);
        assertTrue(ass.isCheckList());
    }



}