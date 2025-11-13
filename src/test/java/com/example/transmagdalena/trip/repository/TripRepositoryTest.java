//package com.example.transmagdalena.trip.repository;
//
//import com.example.transmagdalena.AbstractRepositoryPSQL;
//import com.example.transmagdalena.bus.Bus;
//import com.example.transmagdalena.bus.repository.BusRepository;
//import com.example.transmagdalena.route.Route;
//import com.example.transmagdalena.route.repository.RouteRepository;
//import com.example.transmagdalena.routeStop.RouteStop;
//import com.example.transmagdalena.routeStop.repository.RouteStopRepository;
//import com.example.transmagdalena.seat.Seat;
//import com.example.transmagdalena.seat.repository.SeatRepository;
//import com.example.transmagdalena.seatHold.SeatHold;
//import com.example.transmagdalena.seatHold.SeatHoldStatus;
//import com.example.transmagdalena.seatHold.repository.SeatHoldRepository;
//import com.example.transmagdalena.stop.Stop;
//import com.example.transmagdalena.stop.repository.StopRepository;
//import com.example.transmagdalena.ticket.repository.TicketRepository;
//import com.example.transmagdalena.trip.Trip;
//import com.example.transmagdalena.trip.TripStatus;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.OffsetDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TripRepositoryTest extends AbstractRepositoryPSQL {
//    @Autowired
//    TripRepository tripRepository;
//    @Autowired
//    StopRepository stopRepository;
//    @Autowired
//    RouteStopRepository routeStopRepository;
//    @Autowired
//    RouteRepository routeRepository;
//    @Autowired
//    private SeatHoldRepository seatHoldRepository;
//    @Autowired
//    private BusRepository busRepository;
//    @Autowired
//    private SeatRepository seatRepository;
//
//    @Test
//    @DisplayName("Entontrar viajes segun origen y destino")
//    void entontrarViajesSegunOrigenYDestino() {
//
//        Bus bus1 = Bus.builder().capacity(32).plate("SEX-012").build();
//        Bus savedBus = busRepository.save(bus1);
//
//
//        Route route1 = Route.builder().code("sd").build();
//        Route route1Saved = routeRepository.save(route1);
//
//
//        Stop stop = Stop.builder().name("minca").build();
//        Stop stopSaved = stopRepository.save(stop);
//
//        Stop stop1 = Stop.builder().name("rebolo").build();
//        Stop stopSaved1 = stopRepository.save(stop1);
//
//        Stop stop2 = Stop.builder().name("cienega").build();
//        Stop stopSaved2 = stopRepository.save(stop2);
//
//        Stop stop3 = Stop.builder().name("papa").build();
//        Stop stopSaved3 = stopRepository.save(stop3);
//
//        Trip trip = Trip.builder().tripStatus(TripStatus.SCHEDULED).route(route1Saved).bus(savedBus).departureAt(OffsetDateTime.now()).arrivalAt(OffsetDateTime.now().plusHours(12)).date(OffsetDateTime.now()).build();
//        Trip tripSaved = tripRepository.save(trip);
//
//        RouteStop rs = RouteStop.builder().origin(stopSaved).destination(stopSaved2).route(route1Saved).build();
//        RouteStop rsSaved = routeStopRepository.save(rs);
//
//        RouteStop rs1 = RouteStop.builder().origin(stopSaved1).destination(stopSaved3).route(route1Saved).build();
//        RouteStop rs1Saved = routeStopRepository.save(rs1);
//
//        Pageable pageable = PageRequest.of(0, 1);
//        Page<Trip> foundTrip = tripRepository.findAllTripsBetweenOriginAndDestination(stopSaved.getName(), stopSaved3.getName(), pageable);
//
//        assertEquals(foundTrip.getContent().getFirst().getId(), tripSaved.getId());
//
//    }
//
//
//    @Test
//    @DisplayName("Encontrar asientos ocupados en un viaje")
//    void encontrarAsientosOcupadosEnUnViaje() {
//
//        Trip trip = Trip.builder().tripStatus(TripStatus.SCHEDULED).departureAt(OffsetDateTime.now()).arrivalAt(OffsetDateTime.now().plusHours(12)).date(OffsetDateTime.now()).build();
//        Trip tripSaved = tripRepository.save(trip);
//
//        Seat seat1 = Seat.builder().number(12).build();
//        Seat savedSeat = seatRepository.save(seat1);
//        SeatHold seatHold = new SeatHold();
//        seatHold.setTrip(tripSaved);
//        seatHold.setSeat(savedSeat);
//        seatHoldRepository.save(seatHold);
//
//        List<Integer> foundSeatHold = tripRepository.findSeatHolds(tripSaved.getId());
//
//        assertEquals(foundSeatHold.get(0), savedSeat.getNumber());
//
//
//    }
//
//    @Test
//    @DisplayName("Encontrar viajes de un bus")
//    void encontrarViajesDeUnBus() {
//        Bus bus = Bus.builder().plate("sss-122").capacity(23).build();
//        Bus savedBus = busRepository.save(bus);
//
//        Trip trip = Trip.builder().tripStatus(TripStatus.SCHEDULED).departureAt(OffsetDateTime.now()).arrivalAt(OffsetDateTime.now().plusHours(12)).bus(savedBus).date(OffsetDateTime.now()).build();
//        Trip tripSaved = tripRepository.save(trip);
//        Trip trip1 = Trip.builder().tripStatus(TripStatus.SCHEDULED).departureAt(OffsetDateTime.now()).arrivalAt(OffsetDateTime.now().plusHours(12)).bus(savedBus).date(OffsetDateTime.now()).build();
//        Trip tripSaved1 = tripRepository.save(trip1);
//        Trip trip2 = Trip.builder().tripStatus(TripStatus.SCHEDULED).departureAt(OffsetDateTime.now()).arrivalAt(OffsetDateTime.now().plusHours(12)).bus(savedBus).date(OffsetDateTime.now()).build();
//        Trip tripSaved2 = tripRepository.save(trip2);
//
//        Pageable pageable = PageRequest.of(0, 5);
//
//        Page<Trip> foundTrips = tripRepository.findTripsByBus_Id(pageable, bus.getId());
//        assertEquals(foundTrips.getContent().get(0).getId(), tripSaved.getId());
//        assertEquals(foundTrips.getTotalElements(), 3);
//    }
//
//
//    @Test
//    @DisplayName("encontrar asientos no pagos en una ruta")
//    void  encontrarAsientosNoPagosEnUnaRuta() {
//        Trip trip = Trip.builder().tripStatus(TripStatus.SCHEDULED).departureAt(OffsetDateTime.now()).arrivalAt(OffsetDateTime.now().plusHours(12)).date(OffsetDateTime.now()).build();
//        Trip tripSaved = tripRepository.save(trip);
//
//        Seat seat1 = Seat.builder().number(12).build();
//        Seat savedSeat = seatRepository.save(seat1);
//
//        SeatHold seatHold = SeatHold.builder().trip(tripSaved).seat(savedSeat).status(SeatHoldStatus.EXPIRED).build();
//        SeatHold saved = seatHoldRepository.save(seatHold);
//
//        List<SeatHold> foundUnpaidSeatHols = tripRepository.findUnpaidSeatHolds(tripSaved.getId());
//
//        assertEquals(foundUnpaidSeatHols.getFirst().getId(), saved.getId());
//
//    }
//
//}