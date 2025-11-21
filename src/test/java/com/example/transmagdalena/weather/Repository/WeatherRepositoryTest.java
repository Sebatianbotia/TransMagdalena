package com.example.transmagdalena.weather.Repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.city.City;
import com.example.transmagdalena.city.repository.CityRepository;
import com.example.transmagdalena.weather.Weather;
import com.example.transmagdalena.weather.WeatherType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class WeatherRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    @DisplayName("buscar weather por fecha, hora y ciudad")
    void buscarWeatherPorFechaHoraYCiudad() {

        City city = City.builder()
                .name("Barranquilla")
                .lat(10.9876f)
                .lon(-74.7890f)
                .build();
        city = cityRepository.save(city);

        Weather weather = Weather.builder()
                .weatherType(WeatherType.RAIN)
                .discount(0.1f)
                .date(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(14, 0))
                .city(city)
                .build();
        weather = weatherRepository.save(weather);

        LocalDate targetDate = LocalDate.now().plusDays(1);
        LocalTime targetTime = LocalTime.of(12, 0);

        Optional<Weather> optional = weatherRepository.getWeatherByDateAndTime(targetDate, targetTime, city.getId());

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(weather.getId(), optional.get().getId());
        Assertions.assertEquals(WeatherType.RAIN, optional.get().getWeatherType());
        Assertions.assertEquals(0.1f, optional.get().getDiscount());
        Assertions.assertEquals(city.getId(), optional.get().getCity().getId());
    }

    @Test
    @DisplayName("buscar weather en hora exacta de inicio")
    void buscarWeatherEnHoraExactaDeInicio() {

        City city = City.builder()
                .name("Cartagena")
                .lat(10.3910f)
                .lon(-75.4795f)
                .build();
        city = cityRepository.save(city);

        Weather weather = Weather.builder()
                .weatherType(WeatherType.THUNDERSTORM)
                .discount(0.2f)
                .date(LocalDate.now().plusDays(2))
                .startTime(LocalTime.of(15, 0))
                .endTime(LocalTime.of(18, 0))
                .city(city)
                .build();
        weather = weatherRepository.save(weather);

        LocalDate targetDate = LocalDate.now().plusDays(2);
        LocalTime targetTime = LocalTime.of(15, 0); // Hora exacta de inicio

        Optional<Weather> optional = weatherRepository.getWeatherByDateAndTime(targetDate, targetTime, city.getId());

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(weather.getId(), optional.get().getId());
        Assertions.assertEquals(WeatherType.THUNDERSTORM, optional.get().getWeatherType());
    }

    @Test
    @DisplayName("buscar weather en hora exacta de fin")
    void buscarWeatherEnHoraExactaDeFin() {

        City city = City.builder()
                .name("Santa Marta")
                .lat(11.2408f)
                .lon(-74.1990f)
                .build();
        city = cityRepository.save(city);

        Weather weather = Weather.builder()
                .weatherType(WeatherType.RAIN)
                .discount(0.15f)
                .date(LocalDate.now().plusDays(3))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(12, 0))
                .city(city)
                .build();
        weather = weatherRepository.save(weather);

        LocalDate targetDate = LocalDate.now().plusDays(3);
        LocalTime targetTime = LocalTime.of(12, 0); // Hora exacta de fin

        Optional<Weather> optional = weatherRepository.getWeatherByDateAndTime(targetDate, targetTime, city.getId());

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(weather.getId(), optional.get().getId());
        Assertions.assertEquals(0.15f, optional.get().getDiscount());
    }

    @Test
    @DisplayName("buscar weather - hora fuera del rango")
    void buscarWeatherHoraFueraDelRango() {

        City city = City.builder()
                .name("Sincelejo")
                .lat(9.3047f)
                .lon(-75.3975f)
                .build();
        city = cityRepository.save(city);

        Weather weather = Weather.builder()
                .weatherType(WeatherType.RAIN)
                .discount(0.25f)
                .date(LocalDate.now().plusDays(4))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(11, 0))
                .city(city)
                .build();
        weatherRepository.save(weather);

        LocalDate targetDate = LocalDate.now().plusDays(4);
        LocalTime targetTime = LocalTime.of(14, 0); // Hora fuera del rango

        Optional<Weather> optional = weatherRepository.getWeatherByDateAndTime(targetDate, targetTime, city.getId());

        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    @DisplayName("buscar weather - fecha diferente")
    void buscarWeatherFechaDiferente() {

        City city = City.builder()
                .name("Montería")
                .lat(8.7500f)
                .lon(-75.8833f)
                .build();
        city = cityRepository.save(city);

        Weather weather = Weather.builder()
                .weatherType(WeatherType.RAIN)
                .discount(0.05f)
                .date(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(13, 0))
                .endTime(LocalTime.of(17, 0))
                .city(city)
                .build();
        weatherRepository.save(weather);

        LocalDate targetDate = LocalDate.now().plusDays(6); // Fecha diferente
        LocalTime targetTime = LocalTime.of(15, 0);

        Optional<Weather> optional = weatherRepository.getWeatherByDateAndTime(targetDate, targetTime, city.getId());

        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    @DisplayName("buscar weather - ciudad diferente")
    void buscarWeatherCiudadDiferente() {

        City city1 = City.builder()
                .name("Valledupar")
                .lat(10.4631f)
                .lon(-73.2532f)
                .build();
        city1 = cityRepository.save(city1);

        City city2 = City.builder()
                .name("Riohacha")
                .lat(11.5444f)
                .lon(-72.9072f)
                .build();
        city2 = cityRepository.save(city2);

        Weather weather = Weather.builder()
                .weatherType(WeatherType.RAIN)
                .discount(0.3f)
                .date(LocalDate.now().plusDays(7))
                .startTime(LocalTime.of(16, 0))
                .endTime(LocalTime.of(20, 0))
                .city(city1)
                .build();
        weatherRepository.save(weather);

        LocalDate targetDate = LocalDate.now().plusDays(7);
        LocalTime targetTime = LocalTime.of(18, 0);

        // Buscar en ciudad diferente
        Optional<Weather> optional = weatherRepository.getWeatherByDateAndTime(targetDate, targetTime, city2.getId());

        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    @DisplayName("buscar weather - múltiples weathers, orden por startTime")
    void buscarWeatherMultiplesWeathersOrdenPorStartTime() {

        City city = City.builder()
                .name("Fundación")
                .lat(10.5200f)
                .lon(-74.1800f)
                .build();
        city = cityRepository.save(city);

        Weather weather1 = Weather.builder()
                .weatherType(WeatherType.RAIN)
                .discount(0.1f)
                .date(LocalDate.now().plusDays(8))
                .startTime(LocalTime.of(10, 0)) // Start más temprano
                .endTime(LocalTime.of(12, 0))
                .city(city)
                .build();

        Weather weather2 = Weather.builder()
                .weatherType(WeatherType.RAIN)
                .discount(0.2f)
                .date(LocalDate.now().plusDays(8))
                .startTime(LocalTime.of(11, 0)) // Start más tarde
                .endTime(LocalTime.of(13, 0))
                .city(city)
                .build();

        weatherRepository.save(weather1);
        weatherRepository.save(weather2);

        LocalDate targetDate = LocalDate.now().plusDays(8);
        LocalTime targetTime = LocalTime.of(11, 30); // Hora que está en ambos rangos

        Optional<Weather> optional = weatherRepository.getWeatherByDateAndTime(targetDate, targetTime, city.getId());

        // Debe retornar el que tiene startTime más temprano (ORDER BY startTime ASC)
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(weather1.getId(), optional.get().getId());
        Assertions.assertEquals(LocalTime.of(10, 0), optional.get().getStartTime());
    }
}