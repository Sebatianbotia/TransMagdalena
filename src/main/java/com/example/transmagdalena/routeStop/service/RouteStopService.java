package com.example.transmagdalena.routeStop.service;

import com.example.transmagdalena.route.DTO.RouteDTO;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.RouteStop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RouteStopService {
    RouteStopDTO.routeStopResponse save(RouteStopDTO.routeStopCreateRequest request);
    RouteStopDTO.routeStopResponse update(Long id, RouteStopDTO.routeStopUpdateRequest request);
    Page<RouteStopDTO.routeStopResponse> getAll(Pageable pageable);
    RouteStopDTO.routeStopResponse get(Long id);
    RouteStop getObject(Long id);
    void delete(Long id);
}
