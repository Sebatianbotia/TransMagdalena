package com.example.transmagdalena.city.Mapper;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.city.DTO.CityDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CityMapper {

    City toEntity(CityDTO.cityCreateRequest dto);
    CityDTO.cityResponse toDTO(City city);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(CityDTO.cityResponse response, @MappingTarget City city);
}
