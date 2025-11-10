package com.example.transmagdalena.route.service;

import com.example.transmagdalena.route.DTO.RouteDTO;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.route.mapper.RouteMapper;
import com.example.transmagdalena.route.repository.RouteRepository;
import com.example.transmagdalena.stop.Service.StopService;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.utilities.error.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final StopService stopService;

    @Override
    public RouteDTO.routeResponse save(RouteDTO.routeCreateRequest request) {
        var route = routeMapper.toEntity(request);
        var origin = stopService.getObject(request.originId());
        var destination = stopService.getObject(request.destinationId());
        route.addOrigin(origin);//este metodo se encarga de la bidireccionalidad
        route.addDestination(destination);//este metodo se encarga de la bidireccionalidad
        return routeMapper.toDTO(routeRepository.save(route));
    }

    @Override
    public RouteDTO.routeResponse update(Long id, RouteDTO.routeUpdateRequest request) {
        var route = getObject(id);
        routeMapper.Update(request, route);

        if (request.originId() != null && request.destinationId() != null
                && request.originId().equals(request.destinationId())) {
            throw new IllegalArgumentException("origen y destino no pueden ser iguales");
        }

        if( (request.destinationId()!=null) && ( (!request.destinationId().equals(route.getDestination().getId()) ) ) ){
            route.addDestination(stopService.getObject(request.destinationId()));
        }
        if( (request.originId()!=null) && ( (!request.originId().equals(route.getOrigin().getId()) ) ) ){
            route.addOrigin(stopService.getObject(request.originId()));
        }
        return routeMapper.toDTO(routeRepository.save(route));
    }


    @Override
    public RouteDTO.routeResponse get(Long id) {
        return routeMapper.toDTO(getObject(id));
    }

    @Override
    public Page<RouteDTO.routeResponse> getAll(Pageable pageable) {
        return routeRepository.findAll(pageable).map(routeMapper::toDTO);
    }

    @Override
    public void delete(Long id) {
        routeRepository.deleteById(id);
    }

    @Override
    public Route getObject(Long id) {
        return routeRepository.findById(id).orElseThrow(()-> new NotFoundException("Route not found"));
    }


}
