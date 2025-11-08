package com.example.transmagdalena.city.repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.city.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CityRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private CityRepository cityRepository;

    @Test
    @DisplayName("buscar por nombre")
    void buscarPorNombe(){

        City city = City.builder().name("fundacion").build();
        city = cityRepository.save(city);

        Optional<City> optional = cityRepository.findByName("fundacion");
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(optional.get().getName(), city.getName());
    }

    @Test
    @DisplayName("buscar por id")
    void buscarPorId(){
        City city = City.builder().name("fundacion").build();
        city = cityRepository.save(city);

        Optional<City> optional = cityRepository.findById(city.getId());
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(optional.get().getName(), city.getName());
        Assertions.assertEquals(optional.get().getId(), city.getId());
    }
}
