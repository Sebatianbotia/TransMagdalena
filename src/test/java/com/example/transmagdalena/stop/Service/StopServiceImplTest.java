package com.example.transmagdalena.stop.Service;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.city.service.CityServiceImpl;
import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.stop.mapper.StopMapper;
import com.example.transmagdalena.stop.repository.StopRepository;
import com.example.transmagdalena.utilities.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StopServiceImplTest {

    @Mock StopRepository stopRepository;
    @Mock StopMapper stopMapper;
    @Mock CityServiceImpl cityService;

    @InjectMocks StopServiceImpl service;

    // helper
    private Stop stop() {
        return Stop.builder()
                .id(10L)
                .name("Terminal Norte")
                .city(
                        City.builder()
                                .id(1L)
                                .name("Bogotá")
                                .stops(new HashSet<>())
                                .build()
                )
                .lat(10.5F)
                .lng(20.5F)
                .isDelete(false)
                .build();
    }

    @Test
    void shouldSaveStop() {
        var req = new StopDTO.stopCreateRequest(
                "Paradero Uno",
                1L,
                11.1F,
                22.2F
        );

        City city = City.builder().id(1L).name("Bogotá").stops(new HashSet<>()).build();

        Stop entity = Stop.builder()
                .id(10L)
                .name("Paradero Uno")
                .city(city)
                .lat(11.1F)
                .lng(22.2F)
                .isDelete(false)
                .build();

        StopDTO.stopResponse dto = new StopDTO.stopResponse(
                10L, "Paradero Uno", new StopDTO.cityDto("Bogotá"), 11.1F, 22.2F
        );

        when(stopMapper.toEntity(req)).thenReturn(entity);
        when(cityService.getObject(1L)).thenReturn(city);
        when(stopRepository.save(entity)).thenReturn(entity);
        when(stopMapper.toDTO(entity)).thenReturn(dto);

        var res = service.save(req);

        assertThat(res.id()).isEqualTo(10L);
        assertThat(res.name()).isEqualTo("Paradero Uno");
        assertThat(entity.getCity().getName()).isEqualTo("Bogotá");
    }

    @Test
    void shouldGetStopById() {
        var s = stop();

        StopDTO.stopResponse dto = new StopDTO.stopResponse(
                10L, s.getName(), new StopDTO.cityDto("Bogotá"), s.getLat(), s.getLng()
        );

        when(stopRepository.findById(10L)).thenReturn(Optional.of(s));
        when(stopMapper.toDTO(s)).thenReturn(dto);

        var res = service.get(10L);

        assertThat(res.id()).isEqualTo(10L);
        assertThat(res.name()).isEqualTo("Terminal Norte");
    }

    @Test
    void shouldGetStopByName() {
        var s = stop();

        StopDTO.stopResponse dto = new StopDTO.stopResponse(
                10L, s.getName(), new StopDTO.cityDto("Bogotá"), s.getLat(), s.getLng()
        );

        when(stopRepository.findByNameIgnoreCase("terminal norte"))
                .thenReturn(Optional.of(s));

        when(stopMapper.toDTO(s)).thenReturn(dto);

        var res = service.get("terminal norte");

        assertThat(res.id()).isEqualTo(10L);
        assertThat(res.name()).isEqualTo("Terminal Norte");
    }

    @Test
    void shouldUpdateStop() {
        var s = stop(); // original stop en Bogotá

        var req = new StopDTO.stopUpdateRequest(
                "Nuevo Paradero",
                8L,
                33.3F,
                44.4F
        );

        City newCity = City.builder()
                .id(8L)
                .name("Medellín")
                .stops(new HashSet<>())
                .build();

        // simulate mapper.updateStop()
        doAnswer(inv -> {
            StopDTO.stopUpdateRequest r = inv.getArgument(0);
            Stop entity = inv.getArgument(1);

            if (r.name() != null) entity.setName(r.name());
            if (r.lat() != null) entity.setLat(r.lat());
            if (r.lng() != null) entity.setLng(r.lng());
            return null;
        }).when(stopMapper).updateStop(any(), any());

        when(stopRepository.findById(10L)).thenReturn(Optional.of(s));
        when(cityService.getObject(8L)).thenReturn(newCity);

        StopDTO.stopResponse dto = new StopDTO.stopResponse(
                10L, "Nuevo Paradero",
                new StopDTO.cityDto("Medellín"),
                33.3F, 44.4F
        );

        when(stopMapper.toDTO(s)).thenReturn(dto);

        var res = service.updateStop(req, 10L);

        assertThat(res.name()).isEqualTo("Nuevo Paradero");
        assertThat(s.getCity().getId()).isEqualTo(8L);
        assertThat(s.getLat()).isEqualTo(33.3F);
    }

    @Test
    void shouldDeleteStop() {
        service.delete(55L);
        verify(stopRepository).deleteById(55L);
    }

    @Test
    void shouldGetAll() {
        var s = stop();

        Page<Stop> page = new PageImpl<>(List.of(s));
        when(stopRepository.findAll(PageRequest.of(0,10))).thenReturn(page);

        StopDTO.stopResponse dto = new StopDTO.stopResponse(
                s.getId(), s.getName(), new StopDTO.cityDto("Bogotá"), s.getLat(), s.getLng()
        );
        when(stopMapper.toDTO(s)).thenReturn(dto);

        var result = service.getAll(PageRequest.of(0,10));

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldGetStopsByCity() {
        var s = stop();

        when(stopRepository.findStStopsByCityId(1L)).thenReturn(List.of(s));

        StopDTO.stopResponse dto = new StopDTO.stopResponse(
                s.getId(), s.getName(), new StopDTO.cityDto("Bogotá"), s.getLat(), s.getLng()
        );
        when(stopMapper.toDTO(s)).thenReturn(dto);

        var res = service.getStopsByCity(1L);

        assertThat(res).hasSize(1);
        assertThat(res.get(0).name()).isEqualTo("Terminal Norte");
    }
}
