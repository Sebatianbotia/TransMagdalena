package com.example.transmagdalena.tripQR.Repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.ticket.Ticket;
import com.example.transmagdalena.ticket.repository.TicketRepository;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.TripStatus;
import com.example.transmagdalena.trip.repository.TripRepository;
import com.example.transmagdalena.tripQR.TripQR;
import com.example.transmagdalena.tripQR.TriQRStatus;
import com.example.transmagdalena.tripQR.repository.TripQrRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class TripQRRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private TripQrRepository tripQRRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    @DisplayName("buscar tripQRs por trip id")
    void buscarTripQRsPorTripId() {

        Trip trip = Trip.builder()
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(8, 0))
                .arrivalAt(LocalTime.of(12, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();
        trip = tripRepository.save(trip);

        TripQR tripQR1 = TripQR.builder()
                .trip(trip)
                .qrSeed("QR_SEED_001")
                .status(TriQRStatus.CONFIRMED)
                .build();

        TripQR tripQR2 = TripQR.builder()
                .trip(trip)
                .qrSeed("QR_SEED_002")
                .status(TriQRStatus.CONFIRMED)
                .build();

        tripQRRepository.save(tripQR1);
        tripQRRepository.save(tripQR2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<TripQR> page = tripQRRepository.getTripQRByTripId(trip.getId(), pageable);

        Assertions.assertEquals(2, page.getTotalElements());

        // Solución: extraer el ID en una variable final
        final Long tripId = trip.getId();
        Assertions.assertTrue(page.getContent().stream().allMatch(qr -> qr.getTrip().getId().equals(tripId)));
        Assertions.assertTrue(page.getContent().stream().anyMatch(qr -> "QR_SEED_001".equals(qr.getQrSeed())));
        Assertions.assertTrue(page.getContent().stream().anyMatch(qr -> "QR_SEED_002".equals(qr.getQrSeed())));
    }

    @Test
    @DisplayName("buscar tripQRs por trip id sin resultados")
    void buscarTripQRsPorTripIdSinResultados() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<TripQR> page = tripQRRepository.getTripQRByTripId(999L, pageable);

        Assertions.assertEquals(0, page.getTotalElements());
        Assertions.assertTrue(page.getContent().isEmpty());
    }

    @Test
    @DisplayName("buscar tripQR por qr seed")
    void buscarTripQRPorQrSeed() {

        Trip trip = Trip.builder()
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(9, 0))
                .arrivalAt(LocalTime.of(13, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();
        trip = tripRepository.save(trip);

        TripQR tripQR = TripQR.builder()
                .trip(trip)
                .qrSeed("UNIQUE_QR_SEED_123")
                .status(TriQRStatus.PAYED)
                .build();
        tripQR = tripQRRepository.save(tripQR);

        Optional<TripQR> optional = tripQRRepository.findTripQRByQrSeed("UNIQUE_QR_SEED_123");

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(tripQR.getId(), optional.get().getId());
        Assertions.assertEquals("UNIQUE_QR_SEED_123", optional.get().getQrSeed());
        Assertions.assertEquals(TriQRStatus.PAYED, optional.get().getStatus());
        Assertions.assertEquals(trip.getId(), optional.get().getTrip().getId());
    }

    @Test
    @DisplayName("buscar tripQR por qr seed no existente")
    void buscarTripQRPorQrSeedNoExistente() {

        Optional<TripQR> optional = tripQRRepository.findTripQRByQrSeed("QR_INEXISTENTE");

        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    @DisplayName("buscar tripQR por qr seed con ticket")
    void buscarTripQRPorQrSeedConTicket() {

        Trip trip = Trip.builder()
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(10, 0))
                .arrivalAt(LocalTime.of(14, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();
        trip = tripRepository.save(trip);

        Ticket ticket = Ticket.builder()
                .ticketCode("TICKET_001")
                .build();
        ticket = ticketRepository.save(ticket);

        TripQR tripQR = TripQR.builder()
                .trip(trip)
                .ticket(ticket)
                .qrSeed("QR_WITH_TICKET_456")
                .status(TriQRStatus.PAYED)
                .build();
        tripQR = tripQRRepository.save(tripQR);

        Optional<TripQR> optional = tripQRRepository.findTripQRByQrSeed("QR_WITH_TICKET_456");

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(tripQR.getId(), optional.get().getId());
        Assertions.assertEquals("QR_WITH_TICKET_456", optional.get().getQrSeed());
        Assertions.assertNotNull(optional.get().getTicket());
        Assertions.assertEquals("TICKET_001", optional.get().getTicket().getTicketCode());
    }

    @Test
    @DisplayName("buscar tripQRs excluyendo status CANCELLED")
    void buscarTripQRsExcluyendoCancelled() {

        Trip trip = Trip.builder()
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.of(7, 0))
                .arrivalAt(LocalTime.of(11, 0))
                .tripStatus(TripStatus.SCHEDULED)
                .build();
        trip = tripRepository.save(trip);

        TripQR activeQR = TripQR.builder()
                .trip(trip)
                .qrSeed("ACTIVE_QR")
                .status(TriQRStatus.PAYED)
                .build();

        TripQR cancelledQR = TripQR.builder()
                .trip(trip)
                .qrSeed("CANCELLED_QR")
                .status(TriQRStatus.CANCELLED)
                .build();

        tripQRRepository.save(activeQR);
        tripQRRepository.save(cancelledQR);

        Pageable pageable = PageRequest.of(0, 10);
        Page<TripQR> page = tripQRRepository.getTripQRByTripId(trip.getId(), pageable);

        // Solo debe encontrar el ACTIVE, no el CANCELLED por el @Where(clause = "status != 1")
        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals("ACTIVE_QR", page.getContent().get(0).getQrSeed());
        Assertions.assertEquals(TriQRStatus.PAYED, page.getContent().get(0).getStatus());
    }
}