package com.example.transmagdalena.city.Mapper;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.city.DTO.CityDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CityMapperTest {
    private final CityMapper cityMapper = Mappers.getMapper(CityMapper.class);

    @Test
    void testToEntity() {
        CityDTO.cityCreateRequest createRequest = new CityDTO.cityCreateRequest("Barranquilla", 10.9639f, -74.7964f
        );

        City city = cityMapper.toEntity(createRequest);

        assertNotNull(city);
        assertEquals("Barranquilla", city.getName());
        assertEquals(10.9639f, city.getLat());
        assertEquals(-74.7964f, city.getLon());
    }

    @Test
    void testToDTO() {
        City city = City.builder().id(1L).name("Cartagena").lat(10.3910f).lon(-75.4794f).build();

        CityDTO.cityResponse response = cityMapper.toDTO(city);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Cartagena", response.name());
        assertEquals(10.3910f, response.lat());
        assertEquals(-75.4794f, response.lon());
    }

    @Test
    void testUpdate() {
        City existingCity = City.builder().id(1L).name("OldCity").lat(0.0f).lon(0.0f).build();

        CityDTO.cityUpdateRequest updateResponse = new CityDTO.cityUpdateRequest("Santa Marta", 11.2404f, -74.2110f
        );

        cityMapper.update(updateResponse, existingCity);

        assertEquals(1L, existingCity.getId());
        assertEquals("Santa Marta", existingCity.getName());
        assertEquals(11.2404f, existingCity.getLat());
        assertEquals(-74.2110f, existingCity.getLon());
    }

}