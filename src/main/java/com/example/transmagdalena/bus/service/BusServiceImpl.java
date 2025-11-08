package com.example.transmagdalena.bus.service;

import com.example.transmagdalena.bus.DTO.BusDTO;
import com.example.transmagdalena.bus.mapper.BusMapper;
import com.example.transmagdalena.bus.repository.BusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;
    @Autowired
    private BusMapper busMapper;

    @Override
    public BusDTO.busResponse save(BusDTO.busCreateRequest request) {
        return null;
    }

    @Override
    public BusDTO.busResponse get(Long id) {
        return null;
    }

    @Override
    public Page<BusDTO.busResponse> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public BusDTO.busResponse update(BusDTO.busUpdateRequest request) {
        return null;
    }
}
