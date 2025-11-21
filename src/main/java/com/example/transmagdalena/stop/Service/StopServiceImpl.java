package com.example.transmagdalena.stop.Service;

import com.example.transmagdalena.city.service.CityServiceImpl;
import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.stop.mapper.StopMapper;
import com.example.transmagdalena.stop.repository.StopRepository;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StopServiceImpl implements  StopService {

    private final StopRepository stopRepository;

    private final StopMapper stopMapper;

    private final CityServiceImpl cityService;

    @Override
    @Transactional
    public StopDTO.stopResponse save(StopDTO.stopCreateRequest stopDTO) {
        var s = stopMapper.toEntity(stopDTO);
        s.setCity(cityService.getObject(stopDTO.cityId()));
        return stopMapper.toDTO(stopRepository.save(s));
    }

    @Override
    @Transactional(readOnly = true)
    public StopDTO.stopResponse get(Long id) {
        return stopMapper.toDTO(getObject(id));
    }

    @Override
    @Transactional(readOnly = true)
    public StopDTO.stopResponse get(String name) {
        return stopMapper.toDTO(getObject(name));
    }

    @Override
    public Page<StopDTO.stopResponse> getAll(Pageable pageable) {
        return stopRepository.findAll(pageable).map(stopMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Stop getObject(Long id) {
        return stopRepository.findById(id).orElseThrow(() -> new NotFoundException("Stop not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Stop getObject(String name) {
         return stopRepository.findByNameIgnoreCase(name).orElseThrow(() -> new NotFoundException("Stop not found"));
    }

    @Override
    public void delete(Long id) {
        stopRepository.deleteById(id);
    }

    @Override
    @Transactional
    public StopDTO.stopResponse updateStop(StopDTO.stopUpdateRequest stopDTO, Long id) {
        var s = getObject(id);
        var city = s.getCity();
        stopMapper.updateStop(stopDTO, s);
        if (!city.getId().equals(stopDTO.cityId()) && stopDTO.cityId() != null) {
            s.setCity(cityService.getObject(stopDTO.cityId()));
        }
        return stopMapper.toDTO(s);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StopDTO.stopResponse> getStopsByCity(Long cityId){
        return stopRepository.findByCityId(cityId).stream().map(stopMapper::toDTO).toList();
    }

    public List<StopDTO.stopResponse> getStops(){
        return stopRepository.findAll().stream().map(stopMapper::toDTO).toList();
    }
}
