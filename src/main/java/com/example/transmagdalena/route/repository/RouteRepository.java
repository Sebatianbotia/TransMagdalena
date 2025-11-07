package com.example.transmagdalena.route.repository;

import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
