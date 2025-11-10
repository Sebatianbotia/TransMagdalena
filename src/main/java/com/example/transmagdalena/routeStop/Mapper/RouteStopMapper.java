package com.example.transmagdalena.routeStop.Mapper;

import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.stop.Stop;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RouteStopMapper {

    // Create
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "originId", target = "origin", ignore = true)
    @Mapping(source = "destinationId", target = "destination", ignore = true)
    @Mapping(source = "routeId", target = "route", ignore = true)

    RouteStop toEntity(RouteStopDTO.routeStopCreateRequest dto);
    FareRule toFareRuleEntity(RouteStopDTO.fareRuleCreateRequest dto);

    // UPDATE
    @Mapping(source = "originId", target = "origin", ignore = true)
    @Mapping(source = "destinationId", target = "destination", ignore = true)
    @Mapping(source = "routeId", target = "route", ignore = true)
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
