package com.example.transmagdalena.trip.service;

import com.example.transmagdalena.trip.DTO.TripDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface TripService {

    void deleteTrip(Long id);
    TripDTO.tripResponse getTrip(Long id);
    TripDTO.tripResponse createTrip(TripDTO.tripCreateRequest request);
    Page<TripDTO.tripResponse> getTrips(PageRequest pageRequest);
}
