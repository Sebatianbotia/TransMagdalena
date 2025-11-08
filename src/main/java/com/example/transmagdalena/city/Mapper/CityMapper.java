package com.example.transmagdalena.city.Mapper;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.city.DTO.CityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CityMapper {

    City toEntity(CityDTO dto);
    CityDTO.cityResponse toDTO(City city);
    void update(CityDTO.cityResponse response, @MappingTarget City city);
}
