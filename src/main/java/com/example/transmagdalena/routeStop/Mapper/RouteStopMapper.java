package com.example.transmagdalena.routeStop.Mapper;

import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.stop.Stop;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RouteStopMapper {

    // Create
    RouteStop toEntity(RouteStopDTO.routeStopCreateRequest dto);
    FareRule toFareRuleEntity(RouteStopDTO.fareRuleCreateRequest dto);

    // UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(RouteStopDTO.routeStopUpdateRequest request, @MappingTarget RouteStop route);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(RouteStopDTO.fareRuleUpdateRequest request, @MappingTarget FareRule route);

    // Response
    RouteStopDTO.routeStopResponse toDto(RouteStop entity);
    RouteStopDTO.stopDTO toStopDTO(Stop entity);
    RouteStopDTO.routeDTO toRouteDTO(Route entity);
    RouteStopDTO.fareRuleDTO toFareRuleDto(FareRule entity);

}
