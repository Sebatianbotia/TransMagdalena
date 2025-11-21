package com.example.transmagdalena.stop.Repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.city.City;
import com.example.transmagdalena.city.repository.CityRepository;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.stop.repository.StopRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class StopRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    @DisplayName("buscar stop por nombre ignore case")
    void buscarStopPorNombreIgnoreCase() {

        City city = City.builder().name("Barranquilla").lat(10.9876f).lon(-74.7890f).build();
        city = cityRepository.save(city);

        Stop stop = Stop.builder()
                .name("Terminal de Transporte")
                .city(city)
                .lat(10.9876f)
                .lng(-74.7890f)
                .isDelete(false) // ← Asegurar que no sea null
                .build();

        stop = stopRepository.save(stop);

        // Debug: verificar que se guardó
        System.out.println("Stop guardado: " + stop.getId() + " - " + stop.getName() + " - isDelete: " + stop.getIsDelete());

        Optional<Stop> optional = stopRepository.findByNameIgnoreCase("TERMINAL DE TRANSPORTE");

        // Debug: verificar qué retorna la consulta
        System.out.println("Encontrado: " + optional.isPresent());
        if (optional.isPresent()) {
            System.out.println("Stop encontrado: " + optional.get().getId() + " - " + optional.get().getName());
        }

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(stop.getId(), optional.get().getId());
        Assertions.assertEquals("Terminal de Transporte", optional.get().getName());
    }

    @Test
    @DisplayName("buscar stop por nombre ignore case no existente")
    void buscarStopPorNombreIgnoreCaseNoExistente() {

        Optional<Stop> optional = stopRepository.findByNameIgnoreCase("nombre_inexistente");
        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    @DisplayName("buscar stops por city id")
    void buscarStopsPorCityId() {

        City city = City.builder().name("Santa Marta").lat(11.2408f).lon(-74.1990f).build();
        city = cityRepository.save(city);

        Stop stop1 = Stop.builder()
                .name("Rodadero")
                .city(city)
                .lat(11.2408f)
                .lng(-74.1990f)
                .isDelete(false)
                .build();

        Stop stop2 = Stop.builder()
                .name("Taganga")
                .city(city)
                .lat(11.2670f)
                .lng(-74.1880f)
                .isDelete(false)
                .build();

        stopRepository.save(stop1);
        stopRepository.save(stop2);

        List<Stop> stops = stopRepository.findStStopsByCityId(city.getId());

        Assertions.assertEquals(2, stops.size());
        Assertions.assertTrue(stops.stream().anyMatch(stop -> "Rodadero".equals(stop.getName())));
        Assertions.assertTrue(stops.stream().anyMatch(stop -> "Taganga".equals(stop.getName())));
    }

    @Test
    @DisplayName("buscar stops por city id sin stops")
    void buscarStopsPorCityIdSinStops() {

        City city = City.builder().name("Fundación").lat(10.5200f).lon(-74.1800f).build();
        city = cityRepository.save(city);

        List<Stop> stops = stopRepository.findStStopsByCityId(city.getId());

        Assertions.assertTrue(stops.isEmpty());
    }
}