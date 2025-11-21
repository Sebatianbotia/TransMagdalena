package com.example.transmagdalena.weather.Mapper;

import com.example.transmagdalena.weather.DTO.WeatherDTO;
import com.example.transmagdalena.weather.Weather;
import com.example.transmagdalena.weather.WeatherType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class WeatherMapperTest {
    private final WeatherMapper weatherMapper = Mappers.getMapper(WeatherMapper.class);

    @Test
    void testToEntity() {
        WeatherDTO.weatherCreateRequest createRequest = new WeatherDTO.weatherCreateRequest(
                WeatherType.RAIN,
                0.15f,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(14, 0),
                1L
        );

        Weather weather = weatherMapper.toEntity(createRequest);

        assertNotNull(weather);
        assertEquals(WeatherType.RAIN, weather.getWeatherType());
        assertEquals(0.15f, weather.getDiscount());
        assertEquals(LocalDate.now().plusDays(1), weather.getDate());
        assertEquals(LocalTime.of(10, 0), weather.getStartTime());
        assertEquals(LocalTime.of(14, 0), weather.getEndTime());
        assertNull(weather.getCity()); // City se mapea por ID, no por objeto
    }

    @Test
    void testToEntityWithNullValues() {
        WeatherDTO.weatherCreateRequest createRequest = new WeatherDTO.weatherCreateRequest(
                WeatherType.THUNDERSTORM,
                0.2f,
                null,
                null,
                null,
                null
        );

        Weather weather = weatherMapper.toEntity(createRequest);

        assertNotNull(weather);
        assertEquals(WeatherType.THUNDERSTORM, weather.getWeatherType());
        assertEquals(0.2f, weather.getDiscount());
        assertNull(weather.getDate());
        assertNull(weather.getStartTime());
        assertNull(weather.getEndTime());
        assertNull(weather.getCity());
    }

    @Test
    void testToDTO() {
        Weather weather = Weather.builder()
                .id(1L)
                .weatherType(WeatherType.RAIN)
                .discount(0.1f)
                .date(LocalDate.now().plusDays(2))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(13, 0))
                .city(null) // City no se incluye en el DTO
                .build();

        WeatherDTO.weatherRespose response = weatherMapper.toDTO(weather);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(WeatherType.RAIN, response.weatherType());
        assertEquals(0.1f, response.discount());
        assertEquals(LocalDate.now().plusDays(2), response.date());
        assertEquals(LocalTime.of(9, 0), response.startTime());
        assertEquals(LocalTime.of(13, 0), response.endTime());
        assertNull(response.cityId()); // CityId no se mapea desde la entidad
    }

    @Test
    void testToDTOWithStormType() {
        Weather weather = Weather.builder()
                .id(2L)
                .weatherType(WeatherType.RAIN)
                .discount(0.25f)
                .date(LocalDate.now().plusDays(3))
                .startTime(LocalTime.of(15, 0))
                .endTime(LocalTime.of(18, 0))
                .build();

        WeatherDTO.weatherRespose response = weatherMapper.toDTO(weather);

        assertNotNull(response);
        assertEquals(2L, response.id());
        assertEquals(WeatherType.RAIN, response.weatherType());
        assertEquals(0.25f, response.discount());
        assertEquals(LocalTime.of(15, 0), response.startTime());
        assertEquals(LocalTime.of(18, 0), response.endTime());
    }


    @Test
    void testUpdate() {
        Weather weather = Weather.builder()
                .id(1L)
                .weatherType(WeatherType.RAIN)
                .discount(0.1f)
                .date(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(12, 0))
                .build();

        WeatherDTO.weatherUpdateRequest updateRequest = new WeatherDTO.weatherUpdateRequest(
                WeatherType.THUNDERSTORM,
                0.3f,
                LocalDate.now().plusDays(2),
                LocalTime.of(10, 0),
                LocalTime.of(14, 0),
                5L
        );

        weatherMapper.update(updateRequest, weather);

        assertEquals(1L, weather.getId()); // ID no debe cambiar
        assertEquals(WeatherType.THUNDERSTORM, weather.getWeatherType());
        assertEquals(0.3f, weather.getDiscount());
        assertEquals(LocalDate.now().plusDays(2), weather.getDate());
        assertEquals(LocalTime.of(10, 0), weather.getStartTime());
        assertEquals(LocalTime.of(14, 0), weather.getEndTime());
        // City no se actualiza porque no está en la entidad
    }

    @Test
    void testUpdateWithNullValues() {
        Weather weather = Weather.builder()
                .id(2L)
                .weatherType(WeatherType.RAIN)
                .discount(0.2f)
                .date(LocalDate.now().plusDays(3))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        WeatherDTO.weatherUpdateRequest updateRequest = new WeatherDTO.weatherUpdateRequest(
                null, null, null, null, null, null
        );

        weatherMapper.update(updateRequest, weather);

        // Los valores originales deben mantenerse por NullValuePropertyMappingStrategy.IGNORE
        assertEquals(2L, weather.getId());
        assertEquals(WeatherType.RAIN, weather.getWeatherType());
        assertEquals(0.2f, weather.getDiscount());
        assertEquals(LocalDate.now().plusDays(3), weather.getDate());
        assertEquals(LocalTime.of(9, 0), weather.getStartTime());
        assertEquals(LocalTime.of(11, 0), weather.getEndTime());
    }

    @Test
    void testUpdateWithPartialValues() {
        Weather weather = Weather.builder()
                .id(3L)
                .weatherType(WeatherType.RAIN)
                .discount(0.15f)
                .date(LocalDate.now().plusDays(4))
                .startTime(LocalTime.of(7, 0))
                .endTime(LocalTime.of(10, 0))
                .build();

        WeatherDTO.weatherUpdateRequest updateRequest = new WeatherDTO.weatherUpdateRequest(
                WeatherType.RAIN,
                null, // discount no se actualiza
                LocalDate.now().plusDays(5),
                null, // startTime no se actualiza
                LocalTime.of(12, 0),
                null
        );

        weatherMapper.update(updateRequest, weather);

        assertEquals(3L, weather.getId());
        assertEquals(WeatherType.RAIN, weather.getWeatherType()); // Actualizado
        assertEquals(0.15f, weather.getDiscount()); // Mantiene valor original
        assertEquals(LocalDate.now().plusDays(5), weather.getDate()); // Actualizado
        assertEquals(LocalTime.of(7, 0), weather.getStartTime()); // Mantiene valor original
        assertEquals(LocalTime.of(12, 0), weather.getEndTime()); // Actualizado
    }

    @Test
    void testToDTOWithZeroDiscount() {
        Weather weather = Weather.builder()
                .id(4L)
                .weatherType(WeatherType.RAIN)
                .discount(0.0f)
                .date(LocalDate.now().plusDays(6))
                .startTime(LocalTime.of(16, 0))
                .endTime(LocalTime.of(19, 0))
                .build();

        WeatherDTO.weatherRespose response = weatherMapper.toDTO(weather);

        assertNotNull(response);
        assertEquals(4L, response.id());
        assertEquals(0.0f, response.discount());
        assertEquals(WeatherType.RAIN, response.weatherType());
    }

    @Test
    void testToDTOWithMaxDiscount() {
        Weather weather = Weather.builder()
                .id(5L)
                .weatherType(WeatherType.THUNDERSTORM)
                .discount(1.0f)
                .date(LocalDate.now().plusDays(7))
                .startTime(LocalTime.of(20, 0))
                .endTime(LocalTime.of(23, 0))
                .build();

        WeatherDTO.weatherRespose response = weatherMapper.toDTO(weather);

        assertNotNull(response);
        assertEquals(5L, response.id());
        assertEquals(1.0f, response.discount());
        assertEquals(WeatherType.THUNDERSTORM, response.weatherType());
    }
}