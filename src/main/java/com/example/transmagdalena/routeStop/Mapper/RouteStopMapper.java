package com.example.transmagdalena.routeStop.Mapper;

import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.stop.Stop;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteStopMapper {

    // Create
    RouteStop toEntity(RouteStopDTO.routeStopCreateRequest dto);
    FareRule toFareRuleEntity(RouteStopDTO.fareRuleCreateRequest dto);

    // Response
    RouteStopDTO.routeStopResponse toDto(RouteStop entity);
    RouteStopDTO.stopDTO toStopDTO(Stop entity);
    RouteStopDTO.routeDTO toRouteDTO(Route entity);
    RouteStopDTO.fareRuleDTO toFareRuleDto(FareRule entity);

}
