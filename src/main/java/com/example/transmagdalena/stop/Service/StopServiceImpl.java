package com.example.transmagdalena.stop.Service;

import com.example.transmagdalena.city.service.CityServiceImpl;
import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.stop.mapper.StopMapper;
import com.example.transmagdalena.stop.repository.StopRepository;
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
public class StopServiceImpl implements  StopService {

    @Autowired
    private final StopRepository stopRepository;

    @Autowired
    private final StopMapper stopMapper;

    @Autowired
    private final CityServiceImpl cityService;

    @Override
    public StopDTO.stopResponse save(StopDTO.stopCreateRequest stopDTO) {
        var s = stopMapper.toEntity(stopDTO);
        s.addCity(cityService.getObject(stopDTO.cityId()));
        return stopMapper.toDTO(stopRepository.save(s));
    }

    @Override
    public StopDTO.stopResponse get(Long id) {
        return stopMapper.toDTO(getObject(id));
    }

    @Override
    public StopDTO.stopResponse get(String name) {
        return stopMapper.toDTO(getObject(name));
    }

    @Override
    public Page<StopDTO.stopResponse> getAll(Pageable pageable) {
        return stopRepository.findAll(pageable).map(stopMapper::toDTO);
    }

    @Override
    public Stop getObject(Long id) {
        var s = stopRepository.findById(id).orElseThrow(() -> new NotFoundException("Stop not found"));
        return s;
    }

    @Override
    public Stop getObject(String name) {
        var s = stopRepository.findByNameIgnoreCase(name).orElseThrow(() -> new NotFoundException("Stop not found"));
        return s;
    }

    @Override
    public boolean delete(Long id) {
        var s = getObject(id);
        stopRepository.delete(s);
        return true;
    }

    @Override
    public boolean delete(String name) {
        var s = getObject(name);
        stopRepository.delete(s);
        return true;
    }

    @Override
    @Transactional
    public StopDTO.stopResponse updateStop(StopDTO.stopUpdateRequest stopDTO, Long id) {
        var s = getObject(id);
        var city = s.getCity();
        stopMapper.updateStop(stopDTO, s);
        if (!city.getId().equals(stopDTO.cityId()) && stopDTO.cityId() != null) {
            s.removeCity(city);
            s.addCity(cityService.getObject(stopDTO.cityId()));
        }
        return stopMapper.toDTO(s);
    }
}
