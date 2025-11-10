package com.example.transmagdalena.city.Mapper;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.city.DTO.CityDTO;
import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.Stop;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CityMapper {

    City toEntity(CityDTO.cityCreateRequest dto);
    CityDTO.cityResponse toDTO(City city);
    Set<CityDTO.stopDTO> toStopDTO(Set<Stop> stops);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(CityDTO.cityUpdateRequest updateRequest, @MappingTarget City city);
}
