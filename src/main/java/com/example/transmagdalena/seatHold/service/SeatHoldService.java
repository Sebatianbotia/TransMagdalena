package com.example.transmagdalena.seatHold.service;

import com.example.transmagdalena.seatHold.DTO.SeatHoldDTO;
import com.example.transmagdalena.seatHold.SeatHold;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeatHoldService {
    SeatHoldDTO.seatHoldResponse save(SeatHoldDTO.seatHoldCreateRequest seatHoldCreateRequest);
    SeatHoldDTO.seatHoldResponse get(Long id);
    Page<SeatHoldDTO.seatHoldResponse> getAll(Pageable pageable);
    void delete(Long id);
    SeatHoldDTO.seatHoldResponse update(SeatHoldDTO.seatHoldUpdateRequest seatHoldUpdateRequest, Long id);
    SeatHold getObject(Long id);
}
