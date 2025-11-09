package com.example.transmagdalena.route.mapper;

import com.example.transmagdalena.route.DTO.RouteDTO.*;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.stop.Stop;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    Route toEntity(routeCreateRequest request);
    //update
    @Mapping(source = "originId", target = "origin", ignore = true)
    @Mapping(source = "destinationId", target = "destination", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void Update(routeUpdateRequest request, @MappingTarget Route route);

    //response
    routeResponse toDTO(Route route);

    stopDto toStopDTO(Stop stop);

}
