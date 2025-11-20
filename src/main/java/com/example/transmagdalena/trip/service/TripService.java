package com.example.transmagdalena.trip.service;

import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.bus.DTO.BusDTO;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TripService {
    TripDTO.tripResponse save(TripDTO.tripCreateRequest tripDTO);
    TripDTO.tripResponse update(TripDTO.tripUpdateRequest tripDTO, Long tripId);
    void delete(Long tripId);
    TripDTO.tripResponse get(Long id);
    Page<TripDTO.tripResponse> getAll(Pageable pageable);
    Trip getObject(Long id);
    List<Integer> getBusySeats(Long tripId, Long origin);
    // hay que colocar sus queries
    List<Integer> findSeatsHold(Long tripId);
    List<SeatHold> findUnpaidSeatsHold(Long tripId);
}
