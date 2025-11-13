//package com.example.transmagdalena.trip.Mapper;
//
//import com.example.transmagdalena.bus.Bus;
//import com.example.transmagdalena.route.Route;
//import com.example.transmagdalena.routeStop.RouteStop;
//import com.example.transmagdalena.stop.Stop;
//import com.example.transmagdalena.trip.DTO.TripDTO;
//import com.example.transmagdalena.trip.Trip;
//import com.example.transmagdalena.trip.TripStatus;
//import org.junit.jupiter.api.Test;
//import org.mapstruct.factory.Mappers;
//
//import java.time.OffsetDateTime;
//import java.time.ZoneOffset;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TripMapperTest {
//    public final TripMapper tripMapper = Mappers.getMapper(TripMapper.class);
//
//    @Test
//    void testToEntity() {
//        OffsetDateTime date = OffsetDateTime.of(2025, 11, 10, 8, 0, 0, 0, ZoneOffset.UTC);
//        OffsetDateTime departure = OffsetDateTime.of(2025, 11, 10, 9, 0, 0, 0, ZoneOffset.UTC);
//        OffsetDateTime arrival = OffsetDateTime.of(2025, 11, 10, 12, 0, 0, 0, ZoneOffset.UTC);
//
//        TripDTO.tripCreateRequest createRequest = new TripDTO.tripCreateRequest(
//                2L,
//                3L,
//                date,
//                departure,
//                arrival,
//                TripStatus.SCHEDULED,
//                12L
//        );
//
//        Trip trip = tripMapper.toEntity(createRequest);
//
//        assertNotNull(trip);
//        assertNull(trip.getId());
//        assertEquals(date, trip.getDate());
//        assertEquals(departure, trip.getDepartureAt());
//        assertEquals(arrival, trip.getArrivalAt());
//        assertNull(trip.getBus());
//        assertNull(trip.getRoute());
//        assertEquals(TripStatus.SCHEDULED, trip.getTripStatus());
//    }
//
//    @Test
//    void testToDTO() {
//        Bus bus = Bus.builder()
//                .id(11L)
//                .plate("BUS-11")
//                .capacity(50)
//                .status("ACTIVE")
//                .build();
//
//        Stop origin = Stop.builder()
//                .id(21L)
//                .name("Origin Stop")
//                .lat(1.11f)
//                .lng(2.22f)
//                .build();
//
//        Stop destination = Stop.builder()
//                .id(22L)
//                .name("Destination Stop")
//                .lat(3.33f)
//                .lng(4.44f)
//                .build();
//
//        Route route = Route.builder()
//                .id(31L)
//                .code("R-31")
//                .origin(origin)
//                .destination(destination)
//                .build();
//
//        OffsetDateTime date = OffsetDateTime.of(2025, 12, 1, 6, 0, 0, 0, ZoneOffset.UTC);
//        OffsetDateTime departure = OffsetDateTime.of(2025, 12, 1, 7, 0, 0, 0, ZoneOffset.UTC);
//        OffsetDateTime arrival = OffsetDateTime.of(2025, 12, 1, 10, 0, 0, 0, ZoneOffset.UTC);
//
//        Trip trip = Trip.builder()
//                .id(101L)
//                .bus(bus)
//                .route(route)
//                .date(date)
//                .departureAt(departure)
//                .arrivalAt(arrival)
//                .tripStatus(TripStatus.BOARDING)
//                .build();
//
//        TripDTO.tripResponse dto = tripMapper.toDTO(trip);
//
//        assertNotNull(dto);
//        assertEquals(101L, dto.id());
//        assertNotNull(dto.bus());
//        assertEquals(11L, dto.bus().id());
//        assertEquals("BUS-11", dto.bus().plate());
//        assertEquals(50, dto.bus().capacity());
//        assertEquals("ACTIVE", dto.bus().status());
//        assertNotNull(dto.route());
//        assertEquals(31L, dto.route().id());
//        assertEquals("R-31", dto.route().code());
//        assertNotNull(dto.route().origin());
//        assertEquals(21L, dto.route().origin().id());
//        assertEquals("Origin Stop", dto.route().origin().name());
//        assertEquals(1.11f, dto.route().origin().lat(), 0.0001f);
//        assertEquals(2.22f, dto.route().origin().lng(), 0.0001f);
//        assertNotNull(dto.route().destination());
//        assertEquals(22L, dto.route().destination().id());
//        assertEquals("Destination Stop", dto.route().destination().name());
//        assertEquals(3.33f, dto.route().destination().lat(), 0.0001f);
//        assertEquals(4.44f, dto.route().destination().lng(), 0.0001f);
//        assertEquals(date, dto.date());
//        assertEquals(departure, dto.departureAt());
//        assertEquals(arrival, dto.arrivalAt());
//
//    }
//
//    @Test
//    void testToBusDTO() {
//        Bus bus = Bus.builder()
//                .id(5L)
//                .plate("PL-5")
//                .capacity(40)
//                .status("INACTIVE")
//                .build();
//
//        TripDTO.busDTO busDto = tripMapper.toBusDTO(bus);
//
//        assertNotNull(busDto);
//        assertEquals(5L, busDto.id());
//        assertEquals("PL-5", busDto.plate());
//        assertEquals(40, busDto.capacity());
//        assertEquals("INACTIVE", busDto.status());
//    }
//
//    @Test
//    void testToRouteDTO() {
//        Stop origin = Stop.builder()
//                .id(101L)
//                .name("A")
//                .lat(10.0f)
//                .lng(20.0f)
//                .build();
//
//        Stop destination = Stop.builder()
//                .id(102L)
//                .name("B")
//                .lat(30.0f)
//                .lng(40.0f)
//                .build();
//
//        Route route = Route.builder()
//                .id(201L)
//                .code("CODE201")
//                .origin(origin)
//                .destination(destination)
//                .build();
//
//        TripDTO.routeDTO rdto = tripMapper.toRouteDTO(route);
//
//        assertNotNull(rdto);
//        assertEquals(201L, rdto.id());
//        assertEquals("CODE201", rdto.code());
//        assertNotNull(rdto.origin());
//        assertEquals(101L, rdto.origin().id());
//        assertEquals("A", rdto.origin().name());
//        assertEquals(10.0f, rdto.origin().lat(), 0.0001f);
//        assertEquals(20.0f, rdto.origin().lng(), 0.0001f);
//        assertNotNull(rdto.destination());
//        assertEquals(102L, rdto.destination().id());
//        assertEquals("B", rdto.destination().name());
//        assertEquals(30.0f, rdto.destination().lat(), 0.0001f);
//        assertEquals(40.0f, rdto.destination().lng(), 0.0001f);
//    }
//
//    @Test
//    void testToStopDTO() {
//        Stop stop = Stop.builder()
//                .id(301L)
//                .name("StopX")
//                .lat(5.5f)
//                .lng(6.6f)
//                .build();
//
//
//        TripDTO.stopDTO sdto = tripMapper.toStopDTO(stop);
//
//        assertNotNull(sdto);
//        assertEquals(301L, sdto.id());
//        assertEquals("StopX", sdto.name());
//        assertEquals(5.5f, sdto.lat(), 0.0001f);
//        assertEquals(6.6f, sdto.lng(), 0.0001f);
//    }
//
//}