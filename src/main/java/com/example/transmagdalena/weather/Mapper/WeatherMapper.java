package com.example.transmagdalena.weather.Mapper;

import com.example.transmagdalena.weather.DTO.WeatherDTO;
import com.example.transmagdalena.weather.Weather;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    @Mapping(source = "weatherType", target = "weatherType")
    Weather toEntity(WeatherDTO.weatherCreateRequest req);

    WeatherDTO.weatherRespose toDTO(Weather entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(WeatherDTO.weatherUpdateRequest req, @MappingTarget Weather entity);
}
