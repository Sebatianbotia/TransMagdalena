package com.example.transmagdalena.trip.Mapper;

import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Trip;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TripMapper {

    Trip toEntity(TripDTO.tripCreateRequest tripDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(TripDTO.tripUpdateRequest tripUpdateRequest, @MappingTarget Trip trip);

    TripDTO.tripResponse toDTO(Trip trip);

    TripDTO.stopDTO toStopDTO(Stop entity);
    TripDTO.routeDTO toRouteDTO(Route entity);
    TripDTO.busDTO toBusDTO(Bus entity);
}
