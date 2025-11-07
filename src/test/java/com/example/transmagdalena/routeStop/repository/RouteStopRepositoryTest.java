package com.example.transmagdalena.routeStop.repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.routeStop.RouteStop;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RouteStopRepositoryTeste extends AbstractRepositoryPSQL {
    @Autowired
    private RouteStopRepository routeStopRepository;


    @Test
    @DisplayName("Buscar Parada por id")
    void findRouteStopById() {
        //arrange
        RouteStop stop = new RouteStop();
        stop.setStopOrder(1);
        RouteStop savedRouteStop = routeStopRepository.save(stop);
        //act

        Optional<RouteStop> routeStop = routeStopRepository.findById(savedRouteStop.getId());
        //asset
        assertTrue(routeStop.isPresent());
        assertThat(routeStop.get().getId()).isEqualTo(savedRouteStop.getId());

    }

}