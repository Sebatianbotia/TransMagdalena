package com.example.transmagdalena.city.service;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.city.DTO.CityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CityService {

    CityDTO.cityResponse save(CityDTO.cityCreateRequest request);
    CityDTO.cityResponse update(CityDTO.cityUpdateRequest cityUpdateRequest, Long id);
    CityDTO.cityResponse deleteById(Long id);
    CityDTO.cityResponse get(Long id);
    CityDTO.cityResponse get(String name);
    Page<CityDTO.cityResponse> getAll(PageRequest pageRequest);
    boolean delete(Long id);
    City getObject(Long id);
}
