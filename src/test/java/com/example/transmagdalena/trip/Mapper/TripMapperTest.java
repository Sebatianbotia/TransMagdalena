package com.example.transmagdalena.trip.Mapper;

import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.TripStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TripMapperTest {
    private final TripMapper tripMapper = Mappers.getMapper(TripMapper.class);

    @Test
    void testToEntity() {
        TripDTO.tripCreateRequest createRequest = new TripDTO.tripCreateRequest(
                1L, 2L, LocalDate.now().plusDays(1),
                LocalTime.of(8, 0), LocalTime.of(12, 0),
                TripStatus.SCHEDULED, 3L
        );

        Trip trip = tripMapper.toEntity(createRequest);

        assertNotNull(trip);
        assertNull(trip.getId());
        assertEquals(LocalDate.now().plusDays(1), trip.getDate());
        assertEquals(LocalTime.of(8, 0), trip.getDepartureAt());
        assertEquals(LocalTime.of(12, 0), trip.getArrivalAt());
        assertEquals(TripStatus.SCHEDULED, trip.getTripStatus());
        assertNull(trip.getBus());
        assertNull(trip.getRoute());
        assertNull(trip.getFareRule());
    }

    @Test
    void testUpdate() {
        Trip trip = Trip.builder()
                .id(1L)
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(8, 0))
                .arrivalAt(LocalTime.of(12, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();

        TripDTO.tripUpdateRequest updateRequest = new TripDTO.tripUpdateRequest(
                4L, 5L, LocalDate.now().plusDays(2),
                LocalTime.of(9, 0), LocalTime.of(13, 0),
                TripStatus.CANCELLED, 6L
        );

        tripMapper.update(updateRequest, trip);

        assertEquals(1L, trip.getId());
        assertEquals(LocalDate.now().plusDays(2), trip.getDate());
        assertEquals(LocalTime.of(9, 0), trip.getDepartureAt());
        assertEquals(LocalTime.of(13, 0), trip.getArrivalAt());
        assertEquals(TripStatus.CANCELLED, trip.getTripStatus());
        // Los campos ignorados deben mantenerse null
        assertNull(trip.getBus());
        assertNull(trip.getRoute());
        assertNull(trip.getFareRule());
    }

    @Test
    void testUpdateWithNullValues() {
        Trip trip = Trip.builder()
                .id(1L)
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(8, 0))
                .arrivalAt(LocalTime.of(12, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();

        TripDTO.tripUpdateRequest updateRequest = new TripDTO.tripUpdateRequest(
                null, null, null, null, null, null, null
        );

        tripMapper.update(updateRequest, trip);

        // Los valores originales deben mantenerse por NullValuePropertyMappingStrategy.IGNORE
        assertEquals(1L, trip.getId());
        assertEquals(LocalDate.now().plusDays(1), trip.getDate());
        assertEquals(LocalTime.of(8, 0), trip.getDepartureAt());
        assertEquals(LocalTime.of(12, 0), trip.getArrivalAt());
        assertEquals(TripStatus.SCHEDULED, trip.getTripStatus());
    }

    @Test
    void testTripResponse() {
        Stop origin = Stop.builder().name("Barranquilla").build();
        Stop destination = Stop.builder().name("Cartagena").build();

        Route route = Route.builder()
                .origin(origin)
                .destination(destination)
                .build();

        Bus bus = Bus.builder().plate("ABC123").build();

        FareRule fareRule = FareRule.builder()
                .basePrice(new BigDecimal("50000.00"))
                .build();

        Trip trip = Trip.builder()
                .id(1L)
                .route(route)
                .bus(bus)
                .fareRule(fareRule)
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(8, 0))
                .arrivalAt(LocalTime.of(12, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();

        TripDTO.tripResponse response = tripMapper.tripResponse(trip);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Barranquilla", response.origin());
        assertEquals("Cartagena", response.destination());
        assertEquals(LocalTime.of(8, 0), response.departTime());
        assertEquals(LocalTime.of(12, 0), response.arriveTime());
        assertEquals(LocalDate.now().plusDays(1), response.date());
        assertEquals(new BigDecimal("50000.00"), response.price());
        assertEquals(TripStatus.SCHEDULED, response.status());
        assertEquals("ABC123", response.busPlate());
    }

    @Test
    void testTripResponseWithCustomPrice() {
        Stop origin = Stop.builder().name("Santa Marta").build();
        Stop destination = Stop.builder().name("Fundación").build();

        Route route = Route.builder()
                .origin(origin)
                .destination(destination)
                .build();

        Bus bus = Bus.builder().plate("DEF456").build();

        FareRule fareRule = FareRule.builder()
                .basePrice(new BigDecimal("30000.00"))
                .build();

        Trip trip = Trip.builder()
                .id(2L)
                .route(route)
                .bus(bus)
                .fareRule(fareRule)
                .date(LocalDate.now().plusDays(2))
                .departureAt(LocalTime.of(10, 0))
                .arrivalAt(LocalTime.of(14, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();

        BigDecimal customPrice = new BigDecimal("45000.00");
        TripDTO.tripResponse response = tripMapper.tripResponse(trip, customPrice);

        assertNotNull(response);
        assertEquals(2L, response.id());
        assertEquals("Santa Marta", response.origin());
        assertEquals("Fundación", response.destination());
        assertEquals(LocalTime.of(10, 0), response.departTime());
        assertEquals(LocalTime.of(14, 0), response.arriveTime());
        assertEquals(LocalDate.now().plusDays(2), response.date());
        assertEquals(customPrice, response.price()); // Usa el precio personalizado
        assertEquals(TripStatus.SCHEDULED, response.status());
        assertEquals("DEF456", response.busPlate());
    }

    @Test
    void testTripResponseWithAvailableSeat() {
        Stop origin = Stop.builder().name("Sincelejo").build();
        Stop destination = Stop.builder().name("Montería").build();

        Route route = Route.builder()
                .origin(origin)
                .destination(destination)
                .build();

        Bus bus = Bus.builder().plate("GHI789").build();

        FareRule fareRule = FareRule.builder()
                .basePrice(new BigDecimal("40000.00"))
                .build();

        Trip trip = Trip.builder()
                .id(3L)
                .route(route)
                .bus(bus)
                .fareRule(fareRule)
                .date(LocalDate.now().plusDays(3))
                .departureAt(LocalTime.of(7, 0))
                .arrivalAt(LocalTime.of(11, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();

        Integer availableSeats = 25;
        TripDTO.tripResponseWithSeatAvailable response = tripMapper.tripResponseWithAvailableSeat(trip, availableSeats);

        assertNotNull(response);
        assertNotNull(response.trip());
        assertEquals(3L, response.trip().id());
        assertEquals("Sincelejo", response.trip().origin());
        assertEquals("Montería", response.trip().destination());
        assertEquals(availableSeats, response.seatAvailable());
    }

    @Test
    void testTripResponseWithAvailableSeatAndCustomPrice() {
        Stop origin = Stop.builder().name("Valledupar").build();
        Stop destination = Stop.builder().name("Riohacha").build();

        Route route = Route.builder()
                .origin(origin)
                .destination(destination)
                .build();

        Bus bus = Bus.builder().plate("JKL012").build();

        FareRule fareRule = FareRule.builder()
                .basePrice(new BigDecimal("35000.00"))
                .build();

        Trip trip = Trip.builder()
                .id(4L)
                .route(route)
                .bus(bus)
                .fareRule(fareRule)
                .date(LocalDate.now().plusDays(4))
                .departureAt(LocalTime.of(6, 0))
                .arrivalAt(LocalTime.of(10, 0))
                .tripStatus(TripStatus.ARRIVED)
                .build();

        Integer availableSeats = 15;
        BigDecimal customPrice = new BigDecimal("55000.00");
        TripDTO.tripResponseWithSeatAvailable response = tripMapper.tripResponseWithAvailableSeat(trip, availableSeats, customPrice);

        assertNotNull(response);
        assertNotNull(response.trip());
        assertEquals(4L, response.trip().id());
        assertEquals("Valledupar", response.trip().origin());
        assertEquals("Riohacha", response.trip().destination());
        assertEquals(customPrice, response.trip().price()); // Precio personalizado
        assertEquals(availableSeats, response.seatAvailable());
    }
}