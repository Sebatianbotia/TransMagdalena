package com.example.transmagdalena.weather.Service;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.city.service.CityService;
import com.example.transmagdalena.utilities.error.NotFoundException;
import com.example.transmagdalena.weather.DTO.*;
import com.example.transmagdalena.weather.Mapper.WeatherMapper;
import com.example.transmagdalena.weather.Repository.WeatherRepository;
import com.example.transmagdalena.weather.Weather;
import com.example.transmagdalena.weather.WeatherType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class WeatherServiceImplTest {

    @Mock WeatherRepository weatherRepository;
    @Mock WeatherMapper weatherMapper;
    @Mock CityService cityService;

    @InjectMocks WeatherServiceImpl service;

    private Weather weather() {
        return Weather.builder()
                .id(10L)
                .weatherType(WeatherType.RAIN)
                .discount(0.3F)
                .date(LocalDate.of(2025,1,1))
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(12,0))
                .city(City.builder().id(5L).build())
                .build();
    }

    // ------------------------------------------------------
    @Test
    void shouldSaveWeatherProblem() {
        var req = new WeatherDTO.weatherCreateRequest(
                WeatherType.RAIN, 0.2F,
                LocalDate.of(2025,1,1),
                LocalTime.of(10,0),
                LocalTime.of(11,0),
                5L
        );

        Weather entity = weather();
        Weather saved = weather();

        WeatherDTO.weatherRespose dto =
                new WeatherDTO.weatherRespose(
                        10L, WeatherType.RAIN, 0.2F,
                        saved.getDate(), saved.getStartTime(), saved.getEndTime(),
                        5L
                );

        when(weatherMapper.toEntity(req)).thenReturn(entity);
        when(weatherRepository.save(entity)).thenReturn(saved);
        when(weatherMapper.toDTO(saved)).thenReturn(dto);

        var result = service.save(req);

        assertThat(result.id()).isEqualTo(10L);
    }

    // ------------------------------------------------------
    @Test
    void shouldGetWeatherProblem() {
        var w = weather();

        WeatherDTO.weatherRespose dto =
                new WeatherDTO.weatherRespose(
                        10L, w.getWeatherType(), w.getDiscount(),
                        w.getDate(), w.getStartTime(), w.getEndTime(), 5L
                );

        when(weatherRepository.findById(10L)).thenReturn(Optional.of(w));
        when(weatherMapper.toDTO(w)).thenReturn(dto);

        var result = service.get(10L);

        assertThat(result.id()).isEqualTo(10L);
    }

    // ------------------------------------------------------
    @Test
    void shouldReturnWeatherForDateAndTime() {

        var w = weather();

        WeatherDTO.weatherRespose dto =
                new WeatherDTO.weatherRespose(
                        10L, w.getWeatherType(), w.getDiscount(),
                        w.getDate(), w.getStartTime(), w.getEndTime(), 5L
                );

        when(weatherRepository.getWeatherByDateAndTime(
                w.getDate(), w.getStartTime(), 5L
        )).thenReturn(Optional.of(w));

        when(weatherMapper.toDTO(w)).thenReturn(dto);

        var result = service.get(w.getDate(), w.getStartTime(), 5L);

        assertThat(result.id()).isEqualTo(10L);
    }

    // ------------------------------------------------------
    @Test
    void shouldReturnZeroDiscountWhenNoWeather() {

        Weather zero = Weather.builder().discount(0F).build();

        WeatherDTO.weatherRespose dto =
                new WeatherDTO.weatherRespose(
                        null, null, 0F,
                        null, null, null, null
                );

        when(weatherRepository.getWeatherByDateAndTime(
                any(), any(), anyLong()
        )).thenReturn(Optional.empty());

        when(weatherMapper.toDTO(zero)).thenReturn(dto);

        var result = service.get(LocalDate.now(), LocalTime.NOON, 5L);

        assertThat(result.discount()).isEqualTo(0F);
    }

    // ------------------------------------------------------
    @Test
    void shouldUpdateWeatherProblem() {

        var w = Weather.builder()
                .id(10L)
                .weatherType(WeatherType.RAIN)
                .discount(0.2F)
                .date(LocalDate.of(2025,1,1))
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(12,0))
                .city(
                        City.builder()
                                .id(5L)
                                .name("OldCity")
                                .weathers(new HashSet<>())    // ← FUNDAMENTAL
                                .build()
                )
                .build();

        w.getCity().getWeathers().add(w);

        var req = new WeatherDTO.weatherUpdateRequest(
                WeatherType.THUNDERSTORM, 0.5F,
                LocalDate.of(2025,2,2),
                LocalTime.of(15,0),
                LocalTime.of(17,0),
                8L
        );

        var newCity = City.builder()
                .id(8L)
                .name("NewCity")
                .weathers(new HashSet<>()) // ← NECESARIO
                .build();

        doAnswer(inv -> {
            WeatherDTO.weatherUpdateRequest r = inv.getArgument(0);
            Weather entity = inv.getArgument(1);

            if (r.weatherType() != null) entity.setWeatherType(r.weatherType());
            if (r.discount() != null) entity.setDiscount(r.discount());
            if (r.date() != null) entity.setDate(r.date());
            if (r.startTime() != null) entity.setStartTime(r.startTime());
            if (r.endTime() != null) entity.setEndTime(r.endTime());
            return null;
        }).when(weatherMapper).update(any(), any());

        when(weatherRepository.findById(10L)).thenReturn(Optional.of(w));
        when(cityService.getObject(8L)).thenReturn(newCity);
        when(weatherRepository.save(w)).thenReturn(w);

        var expected = new WeatherDTO.weatherRespose(
                10L, WeatherType.THUNDERSTORM, 0.5F,
                req.date(), req.startTime(), req.endTime(), 8L
        );

        when(weatherMapper.toDTO(w)).thenReturn(expected);

        var result = service.update(req, 10L);

        assertThat(result.weatherType()).isEqualTo(WeatherType.THUNDERSTORM);
        assertThat(result.cityId()).isEqualTo(8L);

        assertThat(w.getCity().getId()).isEqualTo(8L);
        assertThat(newCity.getWeathers()).contains(w);
    }



    // ------------------------------------------------------
    @Test
    void shouldDeleteWeatherProblem() {
        service.delete(10L);
        verify(weatherRepository).deleteById(10L);
    }

    // ------------------------------------------------------
    @Test
    void shouldThrowWhenWeatherNotFound() {
        when(weatherRepository.findById(22L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getObject(22L))
                .isInstanceOf(NotFoundException.class);
    }
}
