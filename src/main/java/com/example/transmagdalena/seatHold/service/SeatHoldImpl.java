package com.example.transmagdalena.seatHold.service;

import com.example.transmagdalena.seat.service.SeatService;
import com.example.transmagdalena.seatHold.DTO.SeatHoldDTO;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.seatHold.mapper.SeatHoldMapper;
import com.example.transmagdalena.seatHold.repository.SeatHoldRepository;
import com.example.transmagdalena.trip.service.TripService;
import com.example.transmagdalena.user.Service.UserService;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatHoldImpl implements SeatHoldService {

    @Autowired
    private final SeatHoldRepository seatHoldRepository;

    @Autowired
    private final SeatHoldMapper seatHoldMapper;

    @Autowired
    private final SeatService seatService;

    @Autowired
    private final TripService tripService;

    @Autowired
    private final UserService userService;

    @Override
    public SeatHoldDTO.seatHoldResponse save(SeatHoldDTO.seatHoldCreateRequest seatHoldCreateRequest) {
        var f = seatHoldMapper.toEntity(seatHoldCreateRequest);
        f.addSeat(seatService.getObject(seatHoldCreateRequest.seatId()));
        f.addTrip(tripService.getObject(seatHoldCreateRequest.tripId()));
        f.addUser(userService.getObject(seatHoldCreateRequest.userId()));
        return seatHoldMapper.toDTO(seatHoldRepository.save(f));
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
    public boolean delete(Long id) {
        var f = getObject(id);
        boolean check = false;
        if (f != null) {
            f.removeSeat(f.getSeat());
            f.removeTrip(f.getTrip());
            f.removeUser(f.getUser());
            seatHoldRepository.delete(f);
            check = true;
        }
        return check;
    }

    @Override
    public SeatHoldDTO.seatHoldResponse update(SeatHoldDTO.seatHoldUpdateRequest seatHoldUpdateRequest, Long id) {
    var s = getObject(id);
    var user = s.getUser();
    var trip = s.getTrip();
    var seat = s.getSeat();
    seatHoldMapper.updateEntity(seatHoldUpdateRequest, s);

    if (user.getId() != seatHoldUpdateRequest.userId()) {
        s.removeUser(user);
        s.addUser(userService.getObject(seatHoldUpdateRequest.userId()));
    }
    if (trip.getId() != seatHoldUpdateRequest.tripId()) {
        s.removeTrip(trip);
        s.addTrip(tripService.getObject(seatHoldUpdateRequest.tripId()));
    }
    if (seat.getId() != seatHoldUpdateRequest.seatId()) {
        s.removeSeat(seat);
        s.addSeat(seatService.getObject(seatHoldUpdateRequest.seatId()));
    }
    return seatHoldMapper.toDTO(seatHoldRepository.save(s));

    }

    @Override
    public SeatHold getObject(Long id) {
        return seatHoldRepository.findById(id).orElseThrow(() -> new NotFoundException("SeatHold not found"));
    }
}
