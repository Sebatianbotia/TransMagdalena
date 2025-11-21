package com.example.transmagdalena.city.Service;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.city.DTO.CityDTO;
import com.example.transmagdalena.city.Mapper.CityMapper;
import com.example.transmagdalena.city.repository.CityRepository;
import com.example.transmagdalena.city.service.CityServiceImpl;
import com.example.transmagdalena.utilities.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {

    @Mock
    CityRepository cityRepository;

    @Mock
    CityMapper cityMapper;

    @InjectMocks
    CityServiceImpl service;

    @Test
    void shouldCreateCityAndReturnDto() {
        var req = new CityDTO.cityCreateRequest("Bogotá", 4.1f, -74.1f);

        var entity = City.builder()
                .id(null)
                .name("Bogotá")
                .lat(4.1f)
                .lon(-74.1f)
                .build();

        when(cityMapper.toEntity(req)).thenReturn(entity);

        when(cityRepository.save(entity)).thenAnswer(inv -> {
            City c = inv.getArgument(0);
            c.setId(10L);
            return c;
        });

        var expectedDto = new CityDTO.cityResponse(10L, "Bogotá", 4.1f, -74.1f,new HashSet<>());
        when(cityMapper.toDTO(any())).thenReturn(expectedDto);

        var result = service.save(req);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.name()).isEqualTo("Bogotá");
        verify(cityRepository).save(entity);
    }

    @Test
    void shouldUpdateCity() {
        var existing = City.builder()
                .id(5L).name("Old").lat(1.0f).lon(2.0f)
                .build();

        when(cityRepository.findById(5L)).thenReturn(Optional.of(existing));

        var updateReq = new CityDTO.cityUpdateRequest("New", 3.0f, 4.0f);

        // mapper.update() no devuelve nada
        doAnswer(inv -> {
            CityDTO.cityUpdateRequest req = inv.getArgument(0);
            City c = inv.getArgument(1);
            c.setName(req.name());
            c.setLat(req.lat());
            c.setLon(req.lon());
            return null;
        }).when(cityMapper).update(eq(updateReq), eq(existing));

        var mappedDto = new CityDTO.cityResponse(5L, "New", 3.0f, 4.0f,new HashSet<>());
        when(cityMapper.toDTO(existing)).thenReturn(mappedDto);

        var result = service.update(updateReq, 5L);

        assertThat(result.name()).isEqualTo("New");
        assertThat(result.lat()).isEqualTo(3.0f);
        verify(cityMapper).update(updateReq, existing);
    }

    @Test
    void shouldThrowWhenCityNotFound() {
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("City not found");
    }

    @Test
    void shouldReturnCityByName() {
        var entity = City.builder().id(1L).name("Cali").lat(3.4f).lon(-76.5f).build();
        var dto = new CityDTO.cityResponse(1L, "Cali", 3.4f, -76.5f, new HashSet<>());

        when(cityRepository.findByName("Cali")).thenReturn(Optional.of(entity));
        when(cityMapper.toDTO(entity)).thenReturn(dto);

        var result = service.get("Cali");

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Cali");
    }

    @Test
    void shouldReturnPagedCities() {
        var entity = City.builder().id(2L).name("Medellín").lat(6.2f).lon(-75.5f).build();

        var page = new PageImpl<>(java.util.List.of(entity));
        when(cityRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        var dto = new CityDTO.cityResponse(2L, "Medellín", 6.2f, -75.5f, new HashSet<>() );
        when(cityMapper.toDTO(entity)).thenReturn(dto);

        var result = service.getAll(PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Medellín");
    }

    @Test
    void shouldDeleteCity() {
        var entity = City.builder().id(3L).name("Santa Marta").build();

        when(cityRepository.findById(3L)).thenReturn(Optional.of(entity));

        boolean deleted = service.delete(3L);

        assertThat(deleted).isTrue();
        verify(cityRepository).delete(entity);
    }
}
