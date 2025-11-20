package com.example.transmagdalena.seatHold.service;

import com.example.transmagdalena.seat.service.SeatService;
import com.example.transmagdalena.seatHold.DTO.SeatHoldDTO;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.seatHold.SeatHoldStatus;
import com.example.transmagdalena.seatHold.mapper.SeatHoldMapper;
import com.example.transmagdalena.seatHold.repository.SeatHoldRepository;
import com.example.transmagdalena.trip.service.TripService;
import com.example.transmagdalena.user.Service.UserService;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatHoldImpl implements SeatHoldService {

    private final SeatHoldRepository seatHoldRepository;

    private final SeatHoldMapper seatHoldMapper;

    private final SeatService seatService;

    private final TripService tripService;

    private final UserService userService;
    private final TaskScheduler  taskScheduler;

    @Override
    @Transactional
    public SeatHoldDTO.seatHoldResponse save(SeatHoldDTO.seatHoldCreateRequest request) {
        var f = seatHoldMapper.toEntity(request);
        f.setTrip(tripService.getObject(request.tripId()));
        if (isSeatfree(request.seatId(), request.tripId())) {
            f.setSeat(seatService.getObject(request.seatId()));
            f.setStatus(null);
        }
        f.setUser(userService.getObject(request.userId()));
        f.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        f = seatHoldRepository.save(f);
        deleteExpiredSeatHolds(f);
        return seatHoldMapper.toDTO(f);
    }

    @Override
    public SeatHoldDTO.seatHoldResponse get(Long id) {
        return seatHoldMapper.toDTO(getObject(id));
    }

    @Override
    public Page<SeatHoldDTO.seatHoldResponse> getAll(Pageable pageable) {
        return seatHoldRepository.findAll(pageable).map(seatHoldMapper::toDTO);
    }

    @Override
    public void delete(Long id) {
      seatHoldRepository.deleteById(id);
    }

    @Override
    public SeatHoldDTO.seatHoldResponse update(SeatHoldDTO.seatHoldUpdateRequest seatHoldUpdateRequest, Long id) {
    var s = getObject(id);
    seatHoldMapper.updateEntity(seatHoldUpdateRequest, s);

    if (seatHoldUpdateRequest.userId() != null) {
        s.setUser(userService.getObject(seatHoldUpdateRequest.userId()));
    }
    if (seatHoldUpdateRequest.tripId() != null) {
        s.setTrip(tripService.getObject(seatHoldUpdateRequest.tripId()));
    }
    if (seatHoldUpdateRequest.seatId() != null) {
        s.setSeat(seatService.getObject(seatHoldUpdateRequest.seatId()));
    }
    return seatHoldMapper.toDTO(seatHoldRepository.save(s));

    }

    @Override
    public SeatHold getObject(Long id) {
        return seatHoldRepository.findById(id).orElseThrow(() -> new NotFoundException("SeatHold not found"));
    }

    public boolean isSeatfree(Long seatId, Long tripId) {
        var s = seatHoldRepository.findSeatHoldBySeat_IdAndTripIdAndStatus(seatId, tripId, SeatHoldStatus.HOLD);
        if (s.isEmpty()) {
            return true;
        }
        throw new IllegalArgumentException("Seat is not free");
    }


    @Transactional
    public void deleteExpiredSeatHolds(SeatHold seatHold) {
        Instant time = Instant.now().plus(10, ChronoUnit.MINUTES );

        taskScheduler.schedule(() -> {
            if (seatHold.getStatus() == null) {
                delete(seatHold.getId());
            }
        }
        ,Date.from(time));
    }


}
