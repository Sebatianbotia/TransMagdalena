package com.example.transmagdalena.weather.Service;

import com.example.transmagdalena.weather.DTO.WeatherDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface WeatherService {

    WeatherDTO.weatherRespose save(WeatherDTO.weatherCreateRequest req);
    WeatherDTO.weatherRespose get(Long id);
    WeatherDTO.weatherRespose get(LocalDate date, LocalTime targetTime, Long cityId);
    WeatherDTO.weatherRespose update(WeatherDTO.weatherUpdateRequest req, Long id);
    void delete(Long id);
}
