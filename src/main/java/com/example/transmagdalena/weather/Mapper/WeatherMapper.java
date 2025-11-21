package com.example.transmagdalena.weather.Mapper;

import com.example.transmagdalena.weather.DTO.WeatherDTO;
import com.example.transmagdalena.weather.Weather;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    Weather toEntity(WeatherDTO.weatherCreateRequest req);
    WeatherDTO.weatherRespose toDTO(Weather entity);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(WeatherDTO.weatherUpdateRequest req, @MappingTarget Weather entity);
}
