package com.example.transmagdalena.ticket.Service;

import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.seatHold.service.SeatHoldService;
import com.example.transmagdalena.stop.Service.StopService;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.ticket.*;
import com.example.transmagdalena.ticket.DTO.TicketDTO;
import com.example.transmagdalena.ticket.Mapper.TicketMapper;
import com.example.transmagdalena.ticket.repository.TicketRepository;
import com.example.transmagdalena.ticket.service.TicketServiceImpl;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.service.TripService;
import com.example.transmagdalena.tripQR.Service.TripQRService;
import com.example.transmagdalena.tripQR.TriQRStatus;
import com.example.transmagdalena.tripQR.TripQR;
import com.example.transmagdalena.tripQR.repository.TripQrRepository;
import com.example.transmagdalena.user.Service.UserService;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.utilities.QR.QRCodeGenerator;
import com.example.transmagdalena.utilities.clodinary.CloudinaryStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TicketServiceImplTest {

    @Mock private TicketRepository ticketRepository;
    @Mock private TripQrRepository tripQrRepository;
    @Mock private TripQRService tripQRService;
    @Mock private TicketMapper ticketMapper;
    @Mock private TripService tripService;
    @Mock private StopService stopService;
    @Mock private UserService userService;
    @Mock private QRCodeGenerator generator;
    @Mock private CloudinaryStorageService storageService;
    @Mock private SeatHoldService seatHoldService;

    @InjectMocks
    private TicketServiceImpl service;

    private TicketDTO.ticketCreateRequest buildCreateRequest() {
        return new TicketDTO.ticketCreateRequest(
                1L, 2L, 3L, 10L, 20L,
                BigDecimal.valueOf(30000),
                TicketStatus.SOLD,
                TicketPaymentMethod.CARD
        );
    }

    private Ticket buildTicketEntity() {
        return Ticket.builder()
                .id(100L)
                .price(BigDecimal.valueOf(30000))
                .status(TicketStatus.SOLD)
                .paymentMethod(TicketPaymentMethod.CARD)
                .build();
    }

    @Test
    void shouldCreateTicketSuccessfully() throws Exception {
        var request = buildCreateRequest();

        // mock dependent objects
        var trip = Trip.builder().tickets(new java.util.HashSet<>()).build();
        var seatHold = SeatHold.builder().build();
        var user = User.builder().tickets(new java.util.HashSet<>()).build();
        var origin = Stop.builder().originTickets(new java.util.HashSet<>()).build();
        var destination = Stop.builder().destinationTickets(new java.util.HashSet<>()).build();

        var ticketBeforeSave = buildTicketEntity();
        var ticketSaved = buildTicketEntity();

        // mocks
        when(ticketMapper.toEntity(request)).thenReturn(ticketBeforeSave);
        when(stopService.getObject(10L)).thenReturn(origin);
        when(stopService.getObject(20L)).thenReturn(destination);
        when(userService.getObject(3L)).thenReturn(user);
        when(tripService.getObject(1L)).thenReturn(trip);
        when(seatHoldService.getObject(2L)).thenReturn(seatHold);

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticketSaved);

        when(generator.generateTicketQRContent(anyLong(), anyString())).thenReturn("qrseed");
        when(generator.generateQRCodeImage(anyString())).thenReturn("img".getBytes());
        when(storageService.uploadQRCode(any(), anyString())).thenReturn("http://qr.com/x");

        when(tripQrRepository.save(any(TripQR.class)))
                .thenReturn(TripQR.builder().status(TriQRStatus.PAYED).build());

        when(ticketMapper.toDto(ticketSaved)).thenReturn(
                new TicketDTO.ticketResponse(
                        100L, null, null, null, null,
                        BigDecimal.valueOf(30000),
                        TicketStatus.SOLD,
                        TicketPaymentMethod.CARD,
                        "http://qr.com/x"
                )
        );

        var response = service.create(request);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.price()).isEqualTo(BigDecimal.valueOf(30000));
        assertThat(response.qrCodeUrl()).isEqualTo("http://qr.com/x");

        verify(ticketRepository, times(2)).save(any(Ticket.class)); // ticket + QR update
    }

    @Test
    void shouldGetTicketById() {
        var ticket = buildTicketEntity();

        when(ticketRepository.findById(100L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toDto(ticket)).thenReturn(
                new TicketDTO.ticketResponse(100L, null, null, null, null,
                        ticket.getPrice(), ticket.getStatus(), ticket.getPaymentMethod(), null)
        );

        var response = service.get(100L);

        assertThat(response.id()).isEqualTo(100L);
    }

    @Test
    void shouldUpdateTicket() {
        var ticket = buildTicketEntity();
        ticket.setOrigin(Stop.builder().originTickets(new java.util.HashSet<>()).build());
        ticket.setDestination(Stop.builder().destinationTickets(new java.util.HashSet<>()).build());
        ticket.setUser(User.builder().tickets(new java.util.HashSet<>()).build());
        ticket.setTrip(Trip.builder().tickets(new java.util.HashSet<>()).build());

        var request = new TicketDTO.ticketUpdateRequest(
                1L, 2L, 3L, 10L, 20L,
                BigDecimal.valueOf(50000),
                TicketStatus.SOLD,
                TicketPaymentMethod.CARD
        );

        when(ticketRepository.findById(100L)).thenReturn(Optional.of(ticket));
        when(stopService.getObject(10L)).thenReturn(Stop.builder().originTickets(new java.util.HashSet<>()).build());
        when(stopService.getObject(20L)).thenReturn(Stop.builder().destinationTickets(new java.util.HashSet<>()).build());
        when(userService.getObject(3L)).thenReturn(User.builder().tickets(new java.util.HashSet<>()).build());
        when(tripService.getObject(1L)).thenReturn(Trip.builder().tickets(new java.util.HashSet<>()).build());

        when(ticketRepository.save(ticket)).thenReturn(ticket);

        when(ticketMapper.toDto(ticket)).thenReturn(
                new TicketDTO.ticketResponse(100L, null, null, null, null,
                        BigDecimal.valueOf(50000),
                        TicketStatus.SOLD,
                        TicketPaymentMethod.CARD,
                        null)
        );

        var response = service.update(100L, request);

        assertThat(response.price()).isEqualTo(BigDecimal.valueOf(50000));
    }

    @Test
    void shouldDeleteTicket() {
        var ticket = buildTicketEntity();
        ticket.setQrCodeUrl("http://qr.com/x");

        when(ticketRepository.findById(100L)).thenReturn(Optional.of(ticket));

        service.delete(100L);

        verify(storageService).deleteQRCode("http://qr.com/x");
        verify(ticketRepository).delete(ticket);
    }
}
