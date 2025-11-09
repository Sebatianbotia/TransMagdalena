package com.example.transmagdalena.trip.service;

import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.repository.TripRepository;
import com.example.transmagdalena.utilities.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class TripServiceImpl implements TripService {
    TripRepository tripRepository;
    @Override
    public void deleteTrip(Long id) {
    }

    @Override
    public TripDTO.tripResponse getTrip(Long id) {
        return null;
    }

    @Override
    public TripDTO.tripResponse createTrip(TripDTO.tripCreateRequest request) {
        return null;
    }

    @Override
    public Page<TripDTO.tripResponse> getTrips(PageRequest pageRequest) {
        return null;
    }

    public Trip getTripEntity(Long id){
        return tripRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));
    }
}
