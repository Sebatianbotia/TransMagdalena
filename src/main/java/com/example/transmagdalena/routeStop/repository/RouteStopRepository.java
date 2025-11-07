package com.example.transmagdalena.routeStop.repository;

import com.example.transmagdalena.routeStop.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {
    Optional<RouteStop> findRouteStopById(Long id);
}
