package com.example.transmagdalena.bus.service;

import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.bus.DTO.BusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BusService {

    BusDTO.busResponse save (BusDTO.busCreateRequest request);
    BusDTO.busResponse get(Long id);
    Page<BusDTO.busResponse> getAll(Pageable pageable);
    void delete(Long id);
    BusDTO.busResponse update(BusDTO.busUpdateRequest request, Long busId);
    Bus getObject(Long id);

}
