package com.example.transmagdalena.trip.service;

import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Mapper.TripMapper;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.repository.TripRepository;
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
public class TripServiceImpl implements TripService {

    @Autowired
    private final TripRepository tripRepository;

    @Autowired
    private final TripMapper tripMapper;

    @Override
    public TripDTO.tripResponse save(TripDTO.tripCreateRequest tripDTO) {
        return null;
    }

    @Override
    public TripDTO.tripResponse update(TripDTO.tripUpdateRequest tripDTO, Trip trip) {
        return null;
    }

    @Override
    public boolean delete(Long tripId) {
        return false;
    }

    @Override
    public TripDTO.tripResponse get(Long id) {
        return null;
    }

    @Override
    public Page<TripDTO.tripResponse> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public Trip getObject(Long id) {
        return tripRepository.findById(id).orElseThrow(() -> new NotFoundException("Trip not found"));
    }
}
