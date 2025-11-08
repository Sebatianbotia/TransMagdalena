package com.example.transmagdalena.fareRule.Mapper;

import com.example.transmagdalena.fareRule.DTO.FareRuleDTO;
import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.RouteStop;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FareRuleMapper {

    FareRule toEntity(FareRuleDTO.fareRuleCreateRequest fareRuleDTO);

    FareRuleDTO.fareRuleResponse toDto(FareRule fareRule);
    RouteStopDTO.stopDTO toStopDTO(RouteStop entity);
    RouteStopDTO.routeDTO toRouteDTO(Route entity);
    RouteStopDTO.fareRuleDTO toFareRuleDto(FareRule entity);
}
