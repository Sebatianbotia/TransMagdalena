package com.example.transmagdalena.bus.mapper;

import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.bus.BusStatus;
import com.example.transmagdalena.bus.DTO.BusDTO;
import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seat.SeatType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BusMapperTest {

    public final BusMapper busMapper = Mappers.getMapper(BusMapper.class);
    @Test
    void testToEntity() {
        BusDTO.busCreateRequest createRequest = new BusDTO.busCreateRequest(
                "ABC-123",
                40,
                BusStatus.ACTIVE,
                new HashSet<BusDTO.seatCreateRequest>()
        );

        Bus bus = busMapper.toEntity(createRequest);

        assertNotNull(bus);
        assertNull(bus.getId());
        assertEquals("ABC-123", bus.getPlate());
        assertEquals(40, bus.getCapacity());
        assertEquals(BusStatus.ACTIVE, bus.getStatus());
        assertNull(bus.getTrips()); // trips debe ser null
        assertNull(bus.getSeats()); // seats debe ser null
    }

    @Test
    void testUpdateEntity() {
        Bus existingBus = new Bus();
        existingBus.setId(1L);
        existingBus.setPlate("OLD-123");
        existingBus.setCapacity(30);
        existingBus.setStatus(BusStatus.MAINTENANCE);

        BusDTO.busCreateRequest updateRequest = new BusDTO.busCreateRequest("NEW-456", 50, BusStatus.ACTIVE, null);

        busMapper.updateEntity(updateRequest, existingBus);

        assertEquals(1L, existingBus.getId());
        assertEquals("NEW-456", existingBus.getPlate());
        assertEquals(50, existingBus.getCapacity());
        assertEquals(BusStatus.ACTIVE, existingBus.getStatus());
    }

    @Test
    void testToBusDTO() {
        Bus bus = new Bus();
        bus.setId(1L);
        bus.setPlate("XYZ-789");
        bus.setCapacity(45);
        bus.setStatus(BusStatus.ACTIVE);

        Set<Seat> seats = new HashSet<>();

        Seat seat1 = new Seat();
        seat1.setId(1L);
        seat1.setNumber(1);
        seat1.setType(SeatType.STANDARD);
        seats.add(seat1);

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setNumber(2);
        seat2.setType(SeatType.PREFERENTIAL);
        seats.add(seat2);

        bus.setSeats(seats);
        BusDTO.busResponse response = busMapper.toBusDTO(bus);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("XYZ-789", response.plate());
        assertEquals(45, response.capacity());
        assertEquals(BusStatus.ACTIVE, response.status());
        assertNotNull(response.seats());
        assertEquals(2, response.seats().size());
    }

    @Test
    void testToSeatDTO() {
        Seat seat = new Seat();
        seat.setId(1L);
        seat.setNumber(5);
        seat.setType(SeatType.STANDARD);
        BusDTO.seatResponseDto seatDTO = busMapper.toSeatDTO(seat);

        assertNotNull(seatDTO);
        assertEquals(5, seatDTO.number());
        assertEquals(SeatType.STANDARD, seatDTO.type());
    }

    @Test
    void testToSeatsDTO() {
        Set<Seat> seats = new HashSet<>();

        Seat seat1 = new Seat();
        seat1.setId(1L);
        seat1.setNumber(1);
        seat1.setType(SeatType.PREFERENTIAL);
        seats.add(seat1);

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setNumber(2);
        seat2.setType(SeatType.PREFERENTIAL);
        seats.add(seat2);

        Seat seat3 = new Seat();
        seat3.setId(3L);
        seat3.setNumber(3);
        seat3.setType(SeatType.PREFERENTIAL);
        seats.add(seat3);

        Set<BusDTO.seatResponseDto> seatDTOs = busMapper.toSeatsDTO(seats);

        assertNotNull(seatDTOs);
        assertEquals(3, seatDTOs.size());

    }

}