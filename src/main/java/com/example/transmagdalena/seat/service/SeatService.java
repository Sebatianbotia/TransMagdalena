package com.example.transmagdalena.seat.service;

import com.example.transmagdalena.seat.DTO.SeatDTO;
import com.example.transmagdalena.seat.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SeatService {

    SeatDTO.seatResponse save(SeatDTO.seatCreateRequest seatDTO);
    SeatDTO.seatResponse update(SeatDTO.seatUpdateRequest seatUpdateRequest, Long id);
    boolean delete(Long id);
    SeatDTO.seatResponse get(Long id);
    Page<SeatDTO.seatResponse> getAll(Pageable pageable);
    Seat getSeatByNumberAndBusId(int number, Long busId);
    Seat getObject(Long id);
}
