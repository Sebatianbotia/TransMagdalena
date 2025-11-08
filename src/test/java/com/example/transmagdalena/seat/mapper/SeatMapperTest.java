package com.example.transmagdalena.seat.mapper;

import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.seat.DTO.SeatDTO;
import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seat.SeatType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class SeatMapperTest {
    public final SeatMapper seatMapper = Mappers.getMapper(SeatMapper.class);

    @Test
    void testToEntity() {
        SeatDTO.seatCreateRequest createRequest = new SeatDTO.seatCreateRequest(
                5,
                SeatType.STANDARD,
                1L
        );

        Seat seat = seatMapper.toEntity(createRequest);

        assertNotNull(seat);
        assertNull(seat.getId());
        assertEquals(5, seat.getNumber());
        assertEquals(SeatType.STANDARD, seat.getType());
        assertNull(seat.getBus());
    }

    @Test
    void testUpdateSeat() {
        Seat existing = new Seat();
        existing.setId(10L);
        existing.setNumber(1);
        existing.setType(SeatType.PREFERENTIAL);

        Bus bus = new Bus();
        bus.setPlate("OLD-PLATE");
        bus.setStatus("INACTIVE");
        existing.setBus(bus);

        SeatDTO.seatUpdateRequest updateRequest = new SeatDTO.seatUpdateRequest(
                10L,
                12,
                SeatType.STANDARD,
                2L
        );

        seatMapper.updateSeat(existing, updateRequest);

        assertEquals(10L, existing.getId());
        assertEquals(12, existing.getNumber());
        assertEquals(SeatType.STANDARD, existing.getType());
        assertNotNull(existing.getBus());
        assertEquals("OLD-PLATE", existing.getBus().getPlate());
        assertEquals("INACTIVE", existing.getBus().getStatus());
    }

    @Test
    void testToSeatResponse() {
        Seat seat = new Seat();
        seat.setId(7L);
        seat.setNumber(21);
        seat.setType(SeatType.PREFERENTIAL);

        Bus bus = new Bus();
        bus.setPlate("BUS-001");
        bus.setStatus("ACTIVE");
        seat.setBus(bus);

        SeatDTO.seatResponse response = seatMapper.toSeatResponse(seat);

        assertNotNull(response);
        assertEquals(7L, response.id());
        assertEquals(21, response.number());
        assertEquals(SeatType.PREFERENTIAL, response.type());
        assertNotNull(response.bus());
        assertEquals("BUS-001", response.bus().plate());
        assertEquals("ACTIVE", response.bus().status());
    }

    @Test
    void testToBusDTO() {
        Bus bus = new Bus();
        bus.setPlate("PLATE-123");
        bus.setStatus("ACTIVE");

        SeatDTO.busDto busDto = seatMapper.toBusDTO(bus);

        assertNotNull(busDto);
        assertEquals("PLATE-123", busDto.plate());
        assertEquals("ACTIVE", busDto.status());
    }


}