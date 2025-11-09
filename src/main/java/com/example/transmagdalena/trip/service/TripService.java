package com.example.transmagdalena.trip.service;

import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TripService {
    TripDTO.tripResponse save(TripDTO.tripCreateRequest tripDTO);
    TripDTO.tripResponse update(TripDTO.tripUpdateRequest tripDTO, Trip trip);
    boolean delete(Long tripId);
    TripDTO.tripResponse get(Long id);
    Page<TripDTO.tripResponse> getAll(Pageable pageable);
    Trip getObject(Long id);

    // hay que colocar sus queries
}
