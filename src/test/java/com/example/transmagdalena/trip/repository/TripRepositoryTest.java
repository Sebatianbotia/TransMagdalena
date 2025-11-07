package com.example.transmagdalena.trip.repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.ticket.repository.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.*;

class TripRepositoryTest extends AbstractRepositoryPSQL {
    @Autowired
    private TicketRepository ticketRepository;

    @Test
    @DisplayName("Entontrar viajes segun origen y destino")
    void entontrarViajesSegunOrigenYDestino() {
        Route route = Route.builder().name("takataka").code("xxx").build();
        Route route1 = Route.builder().name("aaaa").code("sd").build();
        Route route2 = Route.builder().name("BBBB").code("deff").build();
        Route route3 = Route.builder().name("CCCC").code("fefef").build();


    }
}