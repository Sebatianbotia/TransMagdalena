package com.example.transmagdalena.baggage.service;

import com.example.transmagdalena.baggage.Baggage;
import com.example.transmagdalena.baggage.DTO.BaggageDTO;
import com.example.transmagdalena.baggage.mapper.BaggageMapper;
import com.example.transmagdalena.baggage.repository.BaggageRepository;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

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
        BigDecimal fee = BigDecimal.ZERO;
        if (request.weight() >= 5){
            fee = BigDecimal.valueOf(22);
        }
        Baggage baggage = baggageMapper.toEntity(request);
        baggage.setFee(fee);
        Baggage saved = baggageRepository.save(baggage);
        return baggageMapper.toDto(saved);
    }

    @Override
    public BaggageDTO.baggageResponse get(Long id) {
        return baggageMapper.toDto(getObject(id));
    }

    @Override
    public BaggageDTO.baggageResponse get(String tagCode) {
        var f = baggageRepository.findByTagCode(tagCode).orElseThrow(() -> new NotFoundException("Baggage not found"));
        return baggageMapper.toDto(f);
    }

    @Override
    public Page<BaggageDTO.baggageResponse> getAll(PageRequest pageRequest) {
        Page<Baggage> baggages = baggageRepository.findAll(pageRequest);
        return baggages.map(baggage -> baggageMapper.toDto(baggage));
    }

    @Override
    public boolean delete(Long id) {
        var f = getObject(id);
        var check = false;
        if (f!=null){
            baggageRepository.deleteById(id);
            check = true;
        }
        return check;
    }

    @Override
    @Transactional
    public BaggageDTO.baggageResponse update(BaggageDTO.baggageUpdateRequest request, Long id) {
        var f =getObject(id);
        baggageMapper.updateEntity(request, f);
        return baggageMapper.toDto(f);
    }

    private Baggage getObject(Long id){
        return baggageRepository.findById(id).orElseThrow(() -> new NotFoundException("baggage not found"));
    }
}
