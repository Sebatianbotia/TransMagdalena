package com.example.transmagdalena.city.repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.city.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class CityRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private CityRepository cityRepository;

    @Test
    @DisplayName("buscar por nombre")
    void buscarPorNombre(){

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

    @Test
    @DisplayName("buscar todos con paginación")
    void buscarTodosConPaginacion(){

        City city1 = City.builder().name("fundacion").build();
        City city2 = City.builder().name("santa marta").build();
        City city3 = City.builder().name("barranquilla").build();

        cityRepository.save(city1);
        cityRepository.save(city2);
        cityRepository.save(city3);

        Pageable pageable = PageRequest.of(0, 2);
        Page<City> page = cityRepository.findAll(pageable);

        Assertions.assertEquals(3, page.getTotalElements());
        Assertions.assertEquals(2, page.getContent().size());
        Assertions.assertEquals(2, page.getTotalPages());
    }

    @Test
    @DisplayName("contar ciudades")
    void contarCiudades(){

        long countInicial = cityRepository.count();

        City city1 = City.builder().name("fundacion").build();
        City city2 = City.builder().name("santa marta").build();

        cityRepository.save(city1);
        cityRepository.save(city2);

        long countFinal = cityRepository.count();

        Assertions.assertEquals(countInicial + 2, countFinal);
    }

    @Test
    @DisplayName("buscar por nombre no existente")
    void buscarPorNombreNoExistente(){

        Optional<City> optional = cityRepository.findByName("ciudad_inexistente");
        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    @DisplayName("paginación vacía")
    void paginacionVacia(){

        Pageable pageable = PageRequest.of(0, 10);
        Page<City> page = cityRepository.findAll(pageable);

        Assertions.assertEquals(0, page.getTotalElements());
        Assertions.assertEquals(0, page.getContent().size());
    }

    @Test
    @DisplayName("contar cuando no hay ciudades")
    void contarCuandoNoHayCiudades(){

        // Asegurarse de que no hay ciudades
        cityRepository.deleteAll();
        long count = cityRepository.count();

        Assertions.assertEquals(0, count);
    }
}