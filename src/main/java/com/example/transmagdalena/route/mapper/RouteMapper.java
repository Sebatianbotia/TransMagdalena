package com.example.transmagdalena.route.mapper;

import com.example.transmagdalena.route.DTO.RouteDTO;
import com.example.transmagdalena.route.DTO.RouteDTO.*;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.stop.Stop;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping( target = "origin", ignore = true)
    @Mapping( target = "destination", ignore = true)
    Route toEntity(routeCreateRequest request);

    //update
    @Mapping( target = "origin", ignore = true)
    @Mapping( target = "destination", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void Update(routeUpdateRequest request, @MappingTarget Route route);

    //response
    default routeResponse toResponse(Route route) {
        return new routeResponse(route.getId(), route.getCode(), route.getOrigin().getName(),
                route.getDestination().getName(), route.getDistanceKm().toString(), route.getDurationTime().toString());
    }

    default routeResponseStops toResponseStops(Route route){
        return new routeResponseStops(toResponse(route), toStopDTO(route.getRouteStops()));
    }

    default List<routeStopDTO> toStopDTO(List<RouteStop> routeStops) {
        return routeStops.stream().map(
                f -> new routeStopDTO(f.getId(), f.getStopOrder(), f.getOrigin().getName(),
                        f.getDestination().getName(), f.getFareRule().getBasePrice(), f.getFareRule().getIsDynamicPricing()
                        )
        ).toList();
    }

}
