//package com.example.transmagdalena.seatHold.repository;
//
//import com.example.transmagdalena.AbstractRepositoryPSQL;
//import com.example.transmagdalena.bus.Bus;
//import com.example.transmagdalena.bus.repository.BusRepository;
//import com.example.transmagdalena.seat.Seat;
//import com.example.transmagdalena.seat.SeatType;
//import com.example.transmagdalena.seat.repository.SeatRepository;
//import com.example.transmagdalena.seatHold.SeatHold;
//import com.example.transmagdalena.seatHold.SeatHoldStatus;
//import com.example.transmagdalena.trip.Trip;
//import com.example.transmagdalena.trip.TripStatus;
//import com.example.transmagdalena.trip.repository.TripRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.OffsetDateTime;
//import java.time.ZonedDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SeatHoldRepositoryTest extends AbstractRepositoryPSQL {
//    @Autowired
//    SeatHoldRepository seatHoldRepository;
//    @Autowired
//    TripRepository tripRepository;
//    @Autowired
//    BusRepository busRepository;
//    @Autowired
//    SeatRepository seatRepository;
//
//    @Test
//    @DisplayName("Encontrar asiento ocupado por numero en un viaje determinado")
//    void shouldFindSeatByNumber() {
//
//        Bus bus = new Bus();
//        bus.setPlate("AN0-XD1");
//        bus.setCapacity(40);
//        bus.setStatus("ACTIVE");
//        Bus savedBus = busRepository.save(bus);
//
//        Trip trip = new Trip();
//        trip.setBus(savedBus);
//        trip.setDate(OffsetDateTime.now());
//        trip.setDepartureAt(OffsetDateTime.now().plusDays(1));
//        trip.setArrivalAt(OffsetDateTime.now().plusHours(1));
//        trip.setTripStatus(TripStatus.SCHEDULED);
//
//        Trip savedTrip = tripRepository.save(trip);
//
//        Seat seat = new Seat();
//        seat.setBus(savedBus);
//        seat.setNumber(13);
//        seat.setType(SeatType.STANDARD);
//        Seat savedSeat = seatRepository.save(seat);
//
//
//        SeatHold seatHold = new SeatHold();
//        seatHold.setTrip(savedTrip);
//        seatHold.setSeat(savedSeat);
//        seatHold.setStatus(SeatHoldStatus.HOLD);
//        seatHold.setExpiresAt(OffsetDateTime.now().plusSeconds(600));
//        SeatHold savedSeatHold = seatHoldRepository.save(seatHold);
//
//        Optional<Seat> foundSeatHold = seatRepository.findByNumberAndBusId(savedSeat.getNumber() ,savedTrip.getId());
//
//        Assertions.assertThat(foundSeatHold).isPresent();
//        Assertions.assertThat(foundSeatHold.get().getId()).isEqualTo(savedSeatHold.getId());
//        Assertions.assertThat(seat.getNumber()).isEqualTo(13);
//    //
//    }
//
//}