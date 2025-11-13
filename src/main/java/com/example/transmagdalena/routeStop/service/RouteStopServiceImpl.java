package com.example.transmagdalena.routeStop.service;

import com.example.transmagdalena.fareRule.Service.FareRuleService;
import com.example.transmagdalena.route.service.RouteService;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.Mapper.RouteStopMapper;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.routeStop.repository.RouteStopRepository;
import com.example.transmagdalena.stop.Service.StopService;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class RouteStopServiceImpl implements RouteStopService{
    private final RouteStopRepository routeStopRepository;
    private final RouteStopMapper routeStopMapper;
    private final RouteService routeService;
    private final StopService stopService;

    @Override
    public RouteStopDTO.routeStopResponse save(RouteStopDTO.routeStopCreateRequest request) {
        if(Objects.equals(request.originId(), request.destinationId())){
            throw new IllegalArgumentException("origen y destino no pueden ser iguales");
        }

        var routeStop = routeStopMapper.toEntity(request);
        var origin = stopService.getObject(request.originId());
        var destination = stopService.getObject(request.destinationId());
        var route =  routeService.getObject(request.routeId());
        routeStop.addRoute(route);
        routeStop.addOrigin(origin);
        routeStop.addDestination(destination);
        if(routeStop.getFareRule()!=null){

            routeStop.getFareRule().getRouteStops().add(routeStop);
            routeStop.getFareRule().setOrigin(origin);
            routeStop.getFareRule().setDestination(destination);
            //según como se construye la implementacion del mapstruct toca manejar la bidireccionalidad así (revisar)
        }

        return routeStopMapper.toDto(routeStopRepository.save(routeStop));
    }


    @Override
    public RouteStopDTO.routeStopResponse update(Long id, RouteStopDTO.routeStopUpdateRequest request) {

        var routeStop = getObject(id);
        routeStopMapper.update(request, routeStop);
            if(request.originId().equals(request.destinationId())) {
                throw new IllegalArgumentException("origen y destino no pueden ser iguales");
            }
            if (request.destinationId() != null){routeStop.addDestination(stopService.getObject(request.destinationId()));}

        if(request.originId()!=null ){routeStop.addOrigin(stopService.getObject(request.originId()));}

        if( request.routeId()!=null){routeStop.addRoute(routeService.getObject(request.routeId()));}
        return routeStopMapper.toDto(routeStopRepository.save(routeStop));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouteStopDTO.routeStopResponse> getAll(Pageable pageable) {
        return routeStopRepository.findAll(pageable).map(routeStopMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public RouteStopDTO.routeStopResponse get(Long id) {
        return  routeStopMapper.toDto(getObject(id));
    }

    @Override
    @Transactional(readOnly = true)
    public RouteStop getObject(Long id) {
        return routeStopRepository.findById(id).orElseThrow(()-> new NotFoundException("RouteStop not found"));
    }

    @Override
    public void delete(Long id) {
        routeStopRepository.deleteById(id);
    }
}
