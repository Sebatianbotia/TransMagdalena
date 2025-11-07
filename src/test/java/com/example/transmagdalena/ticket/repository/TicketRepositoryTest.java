package com.example.transmagdalena.ticket.repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.ticket.Ticket;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.TripStatus;
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

import static org.junit.jupiter.api.Assertions.*;

class TicketRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    UserRepository userRepository;

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
        User usuario = User.builder().name("Juan").email("aaaaa@gmail.com").phone("12325242").passwordHash("1234").build();
        User usuarioSaved = userRepository.save(usuario);
        Trip trip = Trip.builder().date(OffsetDateTime.now()).departureAt(OffsetDateTime.now().plusHours(12)).arrivalAt(OffsetDateTime.now().minusHours(12)).tripStatus(TripStatus.ARRIVED).build();
        Ticket ticket = Ticket.builder().trip(trip).user(usuarioSaved).build();
    }


}