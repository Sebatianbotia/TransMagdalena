package com.example.transmagdalena.city.service;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.city.DTO.CityDTO;
import com.example.transmagdalena.city.Mapper.CityMapper;
import com.example.transmagdalena.city.repository.CityRepository;
import com.example.transmagdalena.utilities.error.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CityMapper cityMapper;


    @Override
    public CityDTO.cityResponse save(CityDTO.cityCreateRequest request) {
        var c = cityMapper.toEntity(request);
        return cityMapper.toDTO(cityRepository.save(c));
    }

    @Override
    @Transactional
    public CityDTO.cityResponse update(CityDTO.cityUpdateRequest cityUpdateRequest) {
        return null;
    }

    @Override
    public CityDTO.cityResponse deleteById(Long id) {
        return null;
    }

    @Override
    public CityDTO.cityResponse get(Long id) {
        return cityMapper.toDTO(getObject(id));
    }

    @Override
    public Page<CityDTO.cityResponse> getAll(PageRequest pageRequest) {
        Page<City> cities = cityRepository.findAll(pageRequest);
        return cities.map(e ->  cityMapper.toDTO(e));
    }

    @Override
    public boolean delete(Long id) {
        var f = getObject(id);
        boolean check = false;
        if (f != null) {
            cityRepository.delete(f);
            check = true;
        }
        return check;
    }

    private City getObject(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> new NotFoundException("City not found"));
    }
}
