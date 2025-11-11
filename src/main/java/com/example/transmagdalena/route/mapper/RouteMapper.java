package com.example.transmagdalena.route.mapper;

import com.example.transmagdalena.route.DTO.RouteDTO.*;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.stop.Stop;
import org.mapstruct.*;

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
    routeResponse toDTO(Route route);

    stopDto toStopDTO(Stop stop);

}
