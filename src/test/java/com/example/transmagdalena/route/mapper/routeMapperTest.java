package com.example.transmagdalena.route.mapper;

import com.example.transmagdalena.route.DTO.RouteDTO;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.stop.Stop;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class routeMapperTest {
    private final RouteMapper routeMapper = Mappers.getMapper(RouteMapper.class);

    @Test
    void testToEntity() {
        RouteDTO.routeCreateRequest createRequest = new RouteDTO.routeCreateRequest("R001", 1L, 2L);

        Route route = routeMapper.toEntity(createRequest);

        assertNotNull(route);
        assertEquals("R001", route.getCode());
    }

    @Test
    void testUpdate() {
        Stop origin = Stop.builder().id(1L).name("Barranquilla").lat(10.9639f).lng(-74.7964f).build();
        Stop destination = Stop.builder().id(2L).name("Cartagena").lat(10.3910f).lng(-75.4794f).build();
        Route existingRoute = Route.builder().id(1L).code("OLD001").origin(origin).destination(destination).build();
        RouteDTO.routeUpdateRequest updateRequest = new RouteDTO.routeUpdateRequest("NEW001", 3L, 4L);

        routeMapper.Update(updateRequest, existingRoute);

        assertEquals("NEW001", existingRoute.getCode());
        assertEquals(1L, existingRoute.getOrigin().getId());
        assertEquals(2L, existingRoute.getDestination().getId());
    }

    @Test
    void testToDTO() {
        Stop origin = Stop.builder().id(1L).name("Santa Marta").lat(11.2404f).lng(-74.2110f).build();
        Stop destination = Stop.builder().id(2L).name("Valledupar").lat(10.4631f).lng(-73.2532f).build();
        Route route = Route.builder().id(1L).code("R999").origin(origin).destination(destination).build();

        RouteDTO.routeResponse response = routeMapper.toDTO(route);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("R999", response.code());
        assertEquals("Valledupar", response.destination().name());
    }

    @Test
    void testToStopDTO() {
        Stop stop = Stop.builder().id(1L).name("Barranquilla").lat(10.9639f).lng(-74.7964f).build();

        RouteDTO.stopDto stopDto = routeMapper.toStopDTO(stop);

        assertNotNull(stopDto);
        assertEquals("Barranquilla", stopDto.name());
        assertEquals(10.9639f, stopDto.lat());
    }

}