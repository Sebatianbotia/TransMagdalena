package com.example.transmagdalena.weather.Service;

import com.example.transmagdalena.city.service.CityService;
import com.example.transmagdalena.utilities.error.NotFoundException;
import com.example.transmagdalena.weather.DTO.WeatherDTO;
import com.example.transmagdalena.weather.Mapper.WeatherMapper;
import com.example.transmagdalena.weather.Repository.WeatherRepository;
import com.example.transmagdalena.weather.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Transactional
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherMapper weatherMapper;
    private final CityService cityService;

    @Override
    public WeatherDTO.weatherRespose save(WeatherDTO.weatherCreateRequest req) {
        var s = weatherMapper.toEntity(req);
        return weatherMapper.toDTO(weatherRepository.save(s));
    }

    @Override
    public WeatherDTO.weatherRespose get(Long id) {
        return weatherMapper.toDTO(getObject(id));
    }

    @Override
    public WeatherDTO.weatherRespose get(LocalDate date, LocalTime targetTime, Long cityId) {
        return weatherMapper.toDTO(
                weatherRepository.getWeatherByDateAndTime(date, targetTime, cityId)
                        .orElseThrow(() -> new NotFoundException("weather problems not founds")));
    }

    @Override
    public WeatherDTO.weatherRespose update(WeatherDTO.weatherUpdateRequest req, Long id) {
        var s = getObject(id);
        weatherMapper.update(req, s);
        if (req.cityId() != null){
        s.setCity(cityService.getObject(req.cityId()));
        }
        return weatherMapper.toDTO(weatherRepository.save(s));
    }

    @Override
    public void delete(Long id) {
        weatherRepository.deleteById(id);
    }

    public Weather getObject(Long id){
        return weatherRepository.findById(id).orElseThrow(() -> new NotFoundException("weatherProblem not found"));
    }
}
