package com.example.transmagdalena.stop.mapper;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.DTO.StopDTO.*;
import com.example.transmagdalena.stop.Stop;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StopMapper {
    @Mapping(source = "cityId", target = "city", ignore = true)
    Stop toEntity(stopCreateRequest request);


    @Mapping(source = "cityId", target = "city", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStop(stopUpdateRequest request, @MappingTarget Stop stop);

    // response
    stopResponse toDTO(Stop entity);

    cityDto toCityDto(City city);




}
