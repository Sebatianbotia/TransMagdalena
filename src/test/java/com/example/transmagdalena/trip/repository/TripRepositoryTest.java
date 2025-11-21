package com.example.transmagdalena.trip.repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.bus.repository.BusRepository;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.route.repository.RouteRepository;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.routeStop.repository.RouteStopRepository;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.seatHold.SeatHoldStatus;
import com.example.transmagdalena.seatHold.repository.SeatHoldRepository;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.stop.repository.StopRepository;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.TripStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TripRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private RouteStopRepository routeStopRepository;

    @Autowired
    private SeatHoldRepository seatHoldRepository;

    @Test
    @DisplayName("buscar trips por bus id")
    void buscarTripsPorBusId() {

        Bus bus = Bus.builder().plate("ABC123").capacity(40).isDelete(false).build();
        bus = busRepository.save(bus);

        Route route = Route.builder().code("R001").build();
        route = routeRepository.save(route);

        Trip trip1 = Trip.builder()
                .bus(bus)
                .route(route)
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(8, 0))
                .arrivalAt(LocalTime.of(12, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();

        Trip trip2 = Trip.builder()
                .bus(bus)
                .route(route)
                .date(LocalDate.now().plusDays(2))
                .departureAt(LocalTime.of(10, 0))
                .arrivalAt(LocalTime.of(14, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();

        tripRepository.save(trip1);
        tripRepository.save(trip2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Trip> page = tripRepository.findTripsByBus_Id(pageable, bus.getId());

        Assertions.assertEquals(2, page.getTotalElements());

        // Solución: usar una variable final
        final Long busId = bus.getId();
        Assertions.assertTrue(page.getContent().stream().allMatch(t -> t.getBus().getId().equals(busId)));
    }

    @Test
    @DisplayName("buscar seat holds por trip id")
    void buscarSeatHoldsPorTripId() {

        Bus bus = Bus.builder().plate("DEF456").capacity(40).build();
        bus = busRepository.save(bus);

        Route route = Route.builder().code("R002").build();
        route = routeRepository.save(route);

        Trip trip = Trip.builder()
                .bus(bus)
                .route(route)
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(9, 0))
                .arrivalAt(LocalTime.of(13, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();
        trip = tripRepository.save(trip);

        List<Integer> seatNumbers = tripRepository.findSeatHolds(trip.getId());

        Assertions.assertNotNull(seatNumbers);
        Assertions.assertTrue(seatNumbers.isEmpty());
    }

    @Test
    @DisplayName("buscar unpaid seat holds por trip id")
    void buscarUnpaidSeatHoldsPorTripId() {

        Bus bus = Bus.builder().plate("GHI789").capacity(40).build();
        bus = busRepository.save(bus);

        Route route = Route.builder().code("R003").build();
        route = routeRepository.save(route);

        Trip trip = Trip.builder()
                .bus(bus)
                .route(route)
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(7, 0))
                .arrivalAt(LocalTime.of(11, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();
        trip = tripRepository.save(trip);

        List<SeatHold> unpaidSeatHolds = tripRepository.findUnpaidSeatHolds(trip.getId());

        Assertions.assertNotNull(unpaidSeatHolds);
        Assertions.assertTrue(unpaidSeatHolds.isEmpty());
    }

    @Test
    @DisplayName("buscar route stops por origin, destination y trip id")
    void buscarRouteStopsPorOriginDestinationTripId() {

        Stop origin = Stop.builder().name("Origin Stop").lat(10.0f).lng(-74.0f).build();
        Stop destination = Stop.builder().name("Destination Stop").lat(11.0f).lng(-75.0f).build();
        Stop intermediate = Stop.builder().name("Intermediate Stop").lat(10.5f).lng(-74.5f).build();

        origin = stopRepository.save(origin);
        destination = stopRepository.save(destination);
        intermediate = stopRepository.save(intermediate);

        Route route = Route.builder().code("R004").build();
        route = routeRepository.save(route);

        RouteStop routeStop1 = RouteStop.builder().route(route).origin(origin).destination(intermediate).stopOrder(1).build();
        RouteStop routeStop2 = RouteStop.builder().route(route).origin(intermediate).destination(destination).stopOrder(2).build();

        routeStopRepository.save(routeStop1);
        routeStopRepository.save(routeStop2);

        Bus bus = Bus.builder().plate("JKL012").capacity(40).build();
        bus = busRepository.save(bus);

        Trip trip = Trip.builder()
                .bus(bus)
                .route(route)
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(6, 0))
                .arrivalAt(LocalTime.of(10, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();
        trip = tripRepository.save(trip);

        List<RouteStop> routeStops = tripRepository.findRouteStopsByUserId(origin.getId(), destination.getId(), trip.getId());

        Assertions.assertNotNull(routeStops);
    }

    @Test
    @DisplayName("buscar busy seats por trip y origin")
    void buscarBusySeatsPorTripYOrigin() {

        Stop origin = Stop.builder().name("Busy Origin").lat(12.0f).lng(-76.0f).build();
        origin = stopRepository.save(origin);

        Route route = Route.builder().code("R005").build();
        route = routeRepository.save(route);

        Bus bus = Bus.builder().plate("MNO345").capacity(40).build();
        bus = busRepository.save(bus);

        Trip trip = Trip.builder()
                .bus(bus)
                .route(route)
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(5, 0))
                .arrivalAt(LocalTime.of(9, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();
        trip = tripRepository.save(trip);

        List<Integer> busySeats = tripRepository.findBusySeats(trip.getId(), origin.getId());

        Assertions.assertNotNull(busySeats);
        Assertions.assertTrue(busySeats.isEmpty());
    }

    @Test
    @DisplayName("buscar trips por bus id - sin resultados")
    void buscarTripsPorBusIdSinResultados() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Trip> page = tripRepository.findTripsByBus_Id(pageable, 999L);

        Assertions.assertEquals(0, page.getTotalElements());
        Assertions.assertTrue(page.getContent().isEmpty());
    }
}