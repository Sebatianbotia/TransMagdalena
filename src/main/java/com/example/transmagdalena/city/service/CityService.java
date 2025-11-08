package com.example.transmagdalena.city.service;

import com.example.transmagdalena.city.DTO.CityDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CityService {

    CityDTO.cityResponse save(CityDTO.cityCreateRequest request);
    CityDTO.cityResponse update(CityDTO.cityUpdateRequest cityUpdateRequest);
    CityDTO.cityResponse deleteById(Long id);
    CityDTO.cityResponse get(Long id);
    Page<CityDTO.cityResponse> getAll(Integer page, Integer elementsPage);
}
