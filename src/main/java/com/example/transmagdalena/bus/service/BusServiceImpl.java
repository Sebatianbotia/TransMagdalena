package com.example.transmagdalena.bus.service;

import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.bus.DTO.BusDTO;
import com.example.transmagdalena.bus.mapper.BusMapper;
import com.example.transmagdalena.bus.repository.BusRepository;
import com.example.transmagdalena.utilities.error.NotFoundException;
import jakarta.transaction.Transactional;
import jakarta.websocket.OnError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;
    private final BusMapper busMapper;

    @Override
    public BusDTO.busResponse save(BusDTO.busCreateRequest request) {
        var entity = busMapper.toEntity(request);
        entity.setIsDelete(false);
        return busMapper.toBusDTO(busRepository.save(entity));
    }

    @Override
    public BusDTO.busResponse get(Long id) {
        return busMapper.toBusDTO(getObject(id));
    }

    @Override
    public Page<BusDTO.busResponse> getAll(Pageable pageable) {
        return  busRepository.findAll(pageable).map(busMapper::toBusDTO);
    }

    @Override
    public void delete(Long id) {
        busRepository.delete(getObject(id));
    }

    @Override
    public BusDTO.busResponse update(BusDTO.busUpdateRequest request, Long busId) {
        return null;
    }

    @Override
    public Bus getObject(Long id) {
        return busRepository.findBusById(id).orElseThrow(() -> new NotFoundException("Bus not found"));
    }

    @Override
    public Long countBuses(){
        return busRepository.count();
    }


}
