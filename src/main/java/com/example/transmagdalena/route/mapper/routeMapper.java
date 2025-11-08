package com.example.transmagdalena.route.mapper;

import com.example.transmagdalena.route.DTO.RouteDTO.*;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.stop.Stop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface routeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "originId", ignore = true)
    @Mapping(target = "destinationId", ignore = true)
    Route toEntity(routeCreateRequest request);
    //update
    @Mapping(target = "originId", ignore = true)
    @Mapping(target = "destinationId", ignore = true)
    void Update(routeUpdateRequest request, @MappingTarget Route route);

    //response
    routeResponse toDTO(Route route);

    stopDto toStopDTO(Stop stop);

}
