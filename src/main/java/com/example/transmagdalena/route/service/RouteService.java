package com.example.transmagdalena.route.service;

import com.example.transmagdalena.route.DTO.RouteDTO;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.stop.Stop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RouteService {
    RouteDTO.routeResponse save(RouteDTO.routeCreateRequest request);
    RouteDTO.routeResponse update(Long id, RouteDTO.routeUpdateRequest request);
    RouteDTO.routeResponse get(Long id);
    Page<RouteDTO.routeResponse> getAll(Pageable pageable);
    void delete(Long id);
    Route getObject (Long id);
    Long count();
    //RouteDTO.routeResponse getRouteByOriginAndDestination(Stop origin, Stop destination);
}
