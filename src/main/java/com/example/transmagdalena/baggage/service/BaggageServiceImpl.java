package com.example.transmagdalena.baggage.service;

import com.example.transmagdalena.baggage.DTO.BaggageDTO;
import com.example.transmagdalena.baggage.mapper.BaggageMapper;
import com.example.transmagdalena.baggage.repository.BaggageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BaggageServiceImpl implements BaggageService {

    @Autowired
    private BaggageRepository baggageRepository;

    @Autowired
    private BaggageMapper baggageMapper;

    @Override
    public BaggageDTO.baggageResponse save(BaggageDTO.baggageCreateRequest request) {
    return null;
    }

    @Override
    public BaggageDTO.baggageResponse get(Long id) {
        return null;
    }

    @Override
    public Page<BaggageDTO.baggageResponse> getAll(PageRequest pageRequest) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public BaggageDTO.baggageResponse update(BaggageDTO.baggageUpdateRequest request) {
        return null;
    }
}
