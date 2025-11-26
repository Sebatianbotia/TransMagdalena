package com.example.transmagdalena.route.Service;

import com.example.transmagdalena.route.DTO.RouteDTO;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.route.mapper.RouteMapper;
import com.example.transmagdalena.route.repository.RouteRepository;
import com.example.transmagdalena.route.service.RouteServiceImpl;
import com.example.transmagdalena.stop.Service.StopService;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.utilities.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

    @Mock
    RouteRepository routeRepository;

    @Mock
    RouteMapper routeMapper;

    @Mock
    StopService stopService;

    @InjectMocks
    RouteServiceImpl service;

    // -------------------------------------------------------------
    // SAVE
    // -------------------------------------------------------------
//    @Test
//    void shouldCreateRoute() {
//        var req = new RouteDTO.routeCreateRequest("R-01", 1L, 2L, new Float("12"), 12);
//
//        var stopOrigin = Stop.builder()
//                .id(1L)
//                .name("Origin")
//                .originRoutes(new HashSet<>())
//                .destinationRoutes(new HashSet<>())
//                .build();
//
//        var stopDestination = Stop.builder()
//                .id(2L)
//                .name("Destination")
//                .originRoutes(new HashSet<>())
//                .destinationRoutes(new HashSet<>())
//                .build();
//
//        var entity = Route.builder()
//                .code("R-01")
//                .trips(new ArrayList<>())
//                .routeStops(new ArrayList<>())
//                .build();
//
//        when(routeMapper.toEntity(req)).thenReturn(entity);
//        when(stopService.getObject(1L)).thenReturn(stopOrigin);
//        when(stopService.getObject(2L)).thenReturn(stopDestination);
//
//        when(routeRepository.save(entity)).thenAnswer(inv -> {
//            Route r = inv.getArgument(0);
//            r.setId(10L);
//            return r;
//        });
//
//        var dto = new RouteDTO.routeResponse(
//                10L, "R-01", "Origin", "Destination", null, null
//        );
//
//        when(routeMapper.toResponse(any())).thenReturn(dto);
//
//        var result = service.save(req);
//
//        assertThat(result.id()).isEqualTo(10L);
//        assertThat(result.code()).isEqualTo("R-01");
//        verify(routeRepository).save(entity);
//    }
//
//    // -------------------------------------------------------------
//    // UPDATE
//    // -------------------------------------------------------------
//    @Test
//    void shouldUpdateRoute() {
//        var existing = Route.builder()
//                .id(5L)
//                .code("OLD")
//                .origin(null)
//                .destination(null)
//                .routeStops(new ArrayList<>())
//                .trips(new ArrayList<>())
//                .build();
//
//        when(routeRepository.findById(5L)).thenReturn(Optional.of(existing));
//
//        var req = new RouteDTO.routeUpdateRequest("NEW", 1L, 2L,12.2,12);
//
//        var stopOrigin = Stop.builder()
//                .id(1L)
//                .name("O")
//                .originRoutes(new HashSet<>())
//                .destinationRoutes(new HashSet<>())
//                .build();
//
//        var stopDestination = Stop.builder()
//                .id(2L)
//                .name("D")
//                .originRoutes(new HashSet<>())
//                .destinationRoutes(new HashSet<>())
//                .build();
//
//        doAnswer(inv -> {
//            var upd = (RouteDTO.routeUpdateRequest) inv.getArgument(0);
//            var ent = (Route) inv.getArgument(1);
//            ent.setCode(upd.code());
//            return null;
//        }).when(routeMapper).Update(req, existing);
//
//        when(stopService.getObject(1L)).thenReturn(stopOrigin);
//        when(stopService.getObject(2L)).thenReturn(stopDestination);
//
//        when(routeRepository.save(existing)).thenReturn(existing);
//
//        var dto = new RouteDTO.routeResponse(5L, "NEW", "O", "D", null, null);
//        when(routeMapper.toResponse(existing)).thenReturn(dto);
//
//        var result = service.update(5L, req);
//
//        assertThat(result.code()).isEqualTo("NEW");
//        verify(routeMapper).Update(req, existing);
//    }

    // -------------------------------------------------------------
    // UPDATE error: same origin and destination
    // -------------------------------------------------------------
//    @Test
//    void shouldThrowWhenOriginEqualsDestinationOnUpdate() {
//        var existing = Route.builder()
//                .id(1L)
//                .routeStops(new ArrayList<>())
//                .trips(new ArrayList<>())
//                .build();
//
//        when(routeRepository.findById(1L)).thenReturn(Optional.of(existing));
//
//        var req = new RouteDTO.routeUpdateRequest("X", 3L, 3L);
//
//        assertThatThrownBy(() -> service.update(1L, req))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("origen y destino no pueden ser iguales");
//    }

    // -------------------------------------------------------------
    // GET
    // -------------------------------------------------------------
    @Test
    void shouldReturnRouteById() {
        var entity = Route.builder()
                .id(7L)
                .code("X01")
                .build();

        when(routeRepository.findById(7L)).thenReturn(Optional.of(entity));

        var dto = new RouteDTO.routeResponse(
                7L, "X01", "A", "B", null, null
        );

        when(routeMapper.toResponse(entity)).thenReturn(dto);

        var result = service.get(7L);

        assertThat(result.id()).isEqualTo(7L);
        assertThat(result.code()).isEqualTo("X01");
    }

    // -------------------------------------------------------------
    // NOT FOUND
    // -------------------------------------------------------------
    @Test
    void shouldThrowWhenRouteNotFound() {
        when(routeRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getObject(100L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Route not found");
    }

    // -------------------------------------------------------------
    // GET ALL
    // -------------------------------------------------------------
    @Test
    void shouldReturnPagedRoutes() {
        var entity = Route.builder()
                .id(1L)
                .code("R")
                .build();

        var page = new PageImpl<>(List.of(entity));

        when(routeRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);

        var dto = new RouteDTO.routeResponse(1L, "R", null, null, null, null);
        when(routeMapper.toResponse(entity)).thenReturn(dto);

        var result = service.getAll(PageRequest.of(0, 5));

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    // -------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------
    @Test
    void shouldDeleteRoute() {
        service.delete(9L);
        verify(routeRepository).deleteById(9L);
    }

    // -------------------------------------------------------------
    // COUNT
    // -------------------------------------------------------------
    @Test
    void shouldReturnRouteCount() {
        when(routeRepository.count()).thenReturn(12L);

        var count = service.count();

        assertThat(count).isEqualTo(12L);
        verify(routeRepository).count();
    }

    // -------------------------------------------------------------
    // GET ROUTE STOPS
    // -------------------------------------------------------------
    @Test
    void shouldReturnRouteStops() {
        var entity = Route.builder()
                .id(15L)
                .code("ROUTE-15")
                .build();

        when(routeRepository.findById(15L)).thenReturn(Optional.of(entity));

        var dto = new RouteDTO.routeResponseStops(
                new RouteDTO.routeResponse(15L, "ROUTE-15", "O", "D", null, null),
                List.of()
        );

        when(routeMapper.toResponseStops(entity)).thenReturn(dto);

        var result = service.getRouteStops(15L);

        assertThat(result.route().id()).isEqualTo(15L);
    }
}
