package com.example.transmagdalena.stop.Service;

import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.Stop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StopService {
    StopDTO.stopResponse save(StopDTO.stopCreateRequest stopDTO);
    StopDTO.stopResponse get(Long id);
    StopDTO.stopResponse get(String name);
    Page<StopDTO.stopResponse> getAll(Pageable pageable);
    Stop getObject(Long id);
    Stop getObject(String name);
    void delete(Long id);
    StopDTO.stopResponse updateStop(StopDTO.stopUpdateRequest stopDTO, Long id);
    List<StopDTO.stopResponse> getStopsByCity(Long cityId);


}
