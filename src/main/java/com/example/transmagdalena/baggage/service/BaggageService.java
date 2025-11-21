package com.example.transmagdalena.baggage.service;

import com.example.transmagdalena.baggage.Baggage;
import com.example.transmagdalena.baggage.DTO.BaggageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface BaggageService {

    BaggageDTO.baggageResponse save(BaggageDTO.baggageCreateRequest request);
    BaggageDTO.baggageResponse get(Long id);
    BaggageDTO.baggageResponse get(String tagCode);
    Page<BaggageDTO.baggageResponse> getAll(PageRequest pageRequest);
    void delete(Long id);
    BaggageDTO.baggageResponse update(BaggageDTO.baggageUpdateRequest request, Long id);
    Baggage getObject(Long id);
}
