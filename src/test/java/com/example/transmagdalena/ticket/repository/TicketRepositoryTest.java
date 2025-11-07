package com.example.transmagdalena.ticket.repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.route.repository.RouteRepository;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.routeStop.repository.RouteStopRepository;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.stop.repository.StopRepository;
import com.example.transmagdalena.ticket.Ticket;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.TripStatus;
import com.example.transmagdalena.trip.repository.TripRepository;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import com.example.transmagdalena.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TicketRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TripRepository tripRepository;
    @Autowired
    StopRepository stopRepository;
    @Autowired
    RouteStopRepository routeStopRepository;
    @Autowired
    RouteRepository routeRepository;

    @Test
    @DisplayName("Encontrar ticket por usuario")
    void findTicketsByUser_Id(){
        User usuario = new User();
        usuario.setName("Juan");
        usuario.setEmail("aaaa@gmail.com");
        usuario.setPhone("3232146352");
        usuario.setPasswordHash("1234");
        usuario.setCreatedAt(OffsetDateTime.now());
        User rol = userRepository.save(usuario);
        Ticket ticket = new Ticket();
        ticket.setUser(usuario);
        ticket.setSeatNumber(23);
        ticketRepository.save(ticket);
        Ticket ticket1 = new Ticket();
        ticket1.setUser(usuario);
        ticket1.setSeatNumber(23);
        ticketRepository.save(ticket1);

        Ticket ticket2 = new Ticket();
        ticket2.setUser(usuario);
        ticket2.setSeatNumber(23);
        ticketRepository.save(ticket2);



        Pageable pageable = PageRequest.of(0, 1);
        Page<Ticket> tickets = ticketRepository.findTicketsByUser_Id(usuario.getId(), pageable);

        assertEquals(3, tickets.getTotalPages());
        assertEquals(1, tickets.getContent().size());



    }

    @Test
    @DisplayName("Encontrar stops de un viaje de un usuario")
    void findTicketsByUser_IdAndStopOrder(){


        User usuario = User.builder().name("Juan").email("aaaaa@gmail.com").phone("12325242").createdAt(OffsetDateTime.now()).passwordHash("1234").build();
        User usuarioSaved = userRepository.save(usuario);



        Stop stop = Stop.builder().name("minca").build();
        Stop stopSaved = stopRepository.save(stop);

        Stop stop1 = Stop.builder().name("rebolo").build();
        Stop stopSaved1 = stopRepository.save(stop1);

        Stop stop2 = Stop.builder().name("cienega").build();
        Stop stopSaved2 = stopRepository.save(stop2);

        Stop stop3 = Stop.builder().name("papa").build();
        Stop stopSaved3 = stopRepository.save(stop3);

        Route r1a = Route.builder().destination(stop).origin(stop3).code("sddss").build();
        Route r1Saved = routeRepository.save(r1a);

        Trip trip = Trip.builder().route(r1Saved).date(OffsetDateTime.now()).departureAt(OffsetDateTime.now().plusHours(12)).arrivalAt(OffsetDateTime.now().minusHours(12)).tripStatus(TripStatus.ARRIVED).build();
        Trip tripSaved = tripRepository.save(trip);

        Ticket ticket = Ticket.builder().trip(tripSaved).origin(stopSaved).destination(stopSaved3).user(usuarioSaved).build();
        Ticket savedTicket= ticketRepository.save(ticket);

        RouteStop rs = RouteStop.builder().route(r1Saved).origin(stopSaved).destination(stopSaved1).stopOrder(1).build();
        RouteStop rsSaved = routeStopRepository.save(rs);

        RouteStop rs2 = RouteStop.builder().route(r1Saved).origin(stopSaved2).destination(stopSaved3).stopOrder(2).build();
        RouteStop rsSaved2 = routeStopRepository.save(rs2);

        Set<RouteStop> routeStops = ticketRepository.findRouteStopsByUserId(usuarioSaved.getId(), tripSaved.getId());

        assertEquals(2, routeStops.size());

    }


}