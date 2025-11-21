//package com.example.transmagdalena.seatHold.mapper;
//
//import com.example.transmagdalena.seat.Seat;
//import com.example.transmagdalena.seat.SeatType;
//import com.example.transmagdalena.seatHold.DTO.SeatHoldDTO;
//import com.example.transmagdalena.seatHold.SeatHold;
//import com.example.transmagdalena.seatHold.SeatHoldStatus;
//import com.example.transmagdalena.trip.Trip;
//import com.example.transmagdalena.user.User;
//import com.example.transmagdalena.user.UserRols;
//import org.junit.jupiter.api.Test;
//import org.mapstruct.factory.Mappers;
//
//import java.time.OffsetDateTime;
//import java.time.ZoneOffset;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SeatHoldMapperTest {
//    public final SeatHoldMapper seatHoldMapper = Mappers.getMapper(SeatHoldMapper.class);
//
//    @Test
//    void testToEntity() {
//        SeatHoldDTO.seatHoldCreateRequest createRequest = new SeatHoldDTO.seatHoldCreateRequest(
//                10L,
//                20L,
//                30L
//        );
//
//        SeatHold entity = seatHoldMapper.toEntity(createRequest);
//
//        assertNotNull(entity);
//        assertNull(entity.getId());
//        assertEquals(SeatHoldStatus.HOLD, entity.getStatus());
//        assertNull(entity.getUser());
//        assertNull(entity.getTrip());
//        assertNull(entity.getSeat());
//    }
//
//    @Test
//    void testUpdateEntity() {
//        User user = User.builder()
//                .name("Alice")
//                .email("alice@example.com")
//                .phone("3000000000")
//                .rol(UserRols.CLERK)
//                .build();
//
//        Trip trip = Trip.builder()
//                .id(5L)
//                .arrivalAt(OffsetDateTime.of(2025,1,1,10,0,0,0, ZoneOffset.UTC))
//                .departureAt(OffsetDateTime.of(2025,1,1,9,0,0,0, ZoneOffset.UTC))
//                .build();
//
//        Seat seat = Seat.builder()
//                .number(7)
//                .type(SeatType.STANDARD)
//                .build();
//
//        SeatHold existing = SeatHold.builder()
//                .id(100L)
//                .user(user)
//                .trip(trip)
//                .seat(seat)
//                .status(SeatHoldStatus.HOLD)
//                .build();
//
//        SeatHoldDTO.seatHoldUpdateRequest updateRequest = new SeatHoldDTO.seatHoldUpdateRequest(
//                SeatHoldStatus.EXPIRED,
//                11L,
//                21L,
//                31L
//        );
//
//        seatHoldMapper.updateEntity(updateRequest, existing);
//
//        assertEquals(100L, existing.getId());
//        assertEquals(SeatHoldStatus.EXPIRED, existing.getStatus());
//        assertNotNull(existing.getUser());
//        assertEquals("Alice", existing.getUser().getName());
//        assertNotNull(existing.getTrip());
//        assertEquals(5L, existing.getTrip().getId());
//        assertNotNull(existing.getSeat());
//        assertEquals(7, existing.getSeat().getNumber());
//    }
//
//    @Test
//    void testToSeatHoldDTO() {
//        User user = User.builder()
//                .name("Carlos")
//                .email("carlos@example.com")
//                .phone("3100000000")
//                .rol(UserRols.CLERK )
//                .build();
//
//        Trip trip = Trip.builder()
//                .id(7L)
//                .arrivalAt(OffsetDateTime.of(2025,5,2,18,30,0,0, ZoneOffset.UTC))
//                .departureAt(OffsetDateTime.of(2025,5,2,16,30,0,0, ZoneOffset.UTC))
//                .build();
//
//        Seat seat = Seat.builder()
//                .number(12)
//                .type(SeatType.PREFERENTIAL)
//                .build();
//
//        SeatHold seatHold = SeatHold.builder()
//                .id(55L)
//                .user(user)
//                .trip(trip)
//                .seat(seat)
//                .status(SeatHoldStatus.HOLD)
//                .build();
//
//        SeatHoldDTO.seatHoldResponse dto = seatHoldMapper.toDTO(seatHold);
//
//        assertNotNull(dto);
//        assertEquals(55L, dto.id());
//        assertEquals(SeatHoldStatus.HOLD, dto.status());
//        assertNotNull(dto.user());
//        assertEquals("Carlos", dto.user().name());
//        assertEquals("carlos@example.com", dto.user().email());
//        assertEquals("3100000000", dto.user().phone());
//        assertNotNull(dto.trip());
//        assertEquals(7L, dto.trip().id());
//        assertNotNull(dto.seat());
//        assertEquals(12, dto.seat().number());
//        assertEquals(SeatType.PREFERENTIAL, dto.seat().type());
//    }
//
//    @Test
//    void testToUserDTO() {
//        User user = User.builder()
//                .name("María")
//                .email("maria@example.com")
//                .phone("3200000000")
//                .rol(UserRols.CLERK)
//                .build();
//
//        SeatHoldDTO.userDTO ud = seatHoldMapper.toDTO(user);
//
//        assertNotNull(ud);
//        assertEquals("María", ud.name());
//        assertEquals("maria@example.com", ud.email());
//        assertEquals("3200000000", ud.phone());
//        assertEquals(UserRols.CLERK, ud.rol());
//    }
//
//    @Test
//    void testToTripDTO() {
//        Trip trip = Trip.builder()
//                .id(9L)
//                .arrivalAt(OffsetDateTime.of(2025,6,10,12,0,0,0, ZoneOffset.UTC))
//                .departureAt(OffsetDateTime.of(2025,6,10,10,0,0,0, ZoneOffset.UTC))
//                .build();
//
//        SeatHoldDTO.tripDTO td = seatHoldMapper.toDTO(trip);
//
//        assertNotNull(td);
//        assertEquals(9L, td.id());
//        assertEquals(OffsetDateTime.of(2025,6,10,12,0,0,0, ZoneOffset.UTC), td.arrivalAt());
//        assertEquals(OffsetDateTime.of(2025,6,10,10,0,0,0, ZoneOffset.UTC), td.departureAt());
//    }
//
//    @Test
//    void testToSeatDto() {
//        Seat seat = Seat.builder()
//                .number(3)
//                .type(SeatType.STANDARD)
//                .build();
//
//        SeatHoldDTO.seatDto sd = seatHoldMapper.toDTO(seat);
//
//        assertNotNull(sd);
//        assertEquals(3, sd.number());
//        assertEquals(SeatType.STANDARD, sd.type());
//    }
//
//
//}