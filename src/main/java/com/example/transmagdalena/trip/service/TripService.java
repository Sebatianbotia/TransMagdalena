package com.example.transmagdalena.trip.service;

import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.UserRols;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface TripService {
    TripDTO.tripResponse save(TripDTO.tripCreateRequest tripDTO);
    TripDTO.tripResponse update(TripDTO.tripUpdateRequest tripDTO, Long tripId);
    void delete(Long tripId);
    TripDTO.tripResponse get(Long id);
    Page<TripDTO.tripResponse> getAll(Pageable pageable);
    Trip getObject(Long id);
    List<Integer> getBusySeats(Long tripId, Long origin);
    Set<Seat> getTripSeats(Long tripId);
    // hay que colocar sus queries
    List<Integer> findSeatsHold(Long tripId);
    List<SeatHold> findUnpaidSeatsHold(Long tripId);
    Page<TripDTO.tripResponseWithSeatAvailable> findTripsBetweenStops(Long origin, Long destination,
                                                                      Pageable pageable, UserRols userRols, LocalDate date);

}
