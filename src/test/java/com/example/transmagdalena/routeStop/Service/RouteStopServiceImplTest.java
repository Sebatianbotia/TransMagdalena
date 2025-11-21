package com.example.transmagdalena.routeStop.Service;

import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.route.service.RouteService;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.Mapper.RouteStopMapper;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.routeStop.repository.RouteStopRepository;
import com.example.transmagdalena.routeStop.service.RouteStopServiceImpl;
import com.example.transmagdalena.stop.Service.StopService;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.utilities.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteStopServiceImplTest {

    @Mock
    RouteStopRepository routeStopRepository;

    @Mock
    RouteStopMapper routeStopMapper;

    @Mock
    RouteService routeService;

    @Mock
    StopService stopService;

    @InjectMocks
    RouteStopServiceImpl service;

    // -------------------------------------------------------------
    // SAVE
    // -------------------------------------------------------------
    @Test
    void shouldCreateRouteStop() {
        var req = new RouteStopDTO.routeStopCreateRequest(
                1, 10L, 20L, 30L,
                new RouteStopDTO.fareRuleCreateRequest(BigDecimal.TEN, true)
        );

        var origin = Stop.builder()
                .id(10L)
                .name("Origin")
                .build();

        var destination = Stop.builder()
                .id(20L)
                .name("Destination")
                .build();

        var route = Route.builder()
                .id(30L)
                .routeStops(new ArrayList<>())
                .trips(new ArrayList<>())
                .build();

        var entity = RouteStop.builder()
                .stopOrder(1)
                .fareRule(null)
                .build();

        when(routeStopMapper.toEntity(req)).thenReturn(entity);
        when(stopService.getObject(10L)).thenReturn(origin);
        when(stopService.getObject(20L)).thenReturn(destination);
        when(routeService.getObject(30L)).thenReturn(route);

        when(routeStopRepository.save(entity)).thenAnswer(inv -> {
            RouteStop rs = inv.getArgument(0);
            rs.setId(99L);
            return rs;
        });

        var dto = new RouteStopDTO.routeStopResponse(
                99L, 1, null, null, null, null
        );

        when(routeStopMapper.toDto(any())).thenReturn(dto);

        var result = service.save(req);

        assertThat(result.id()).isEqualTo(99L);
        assertThat(result.stopOrder()).isEqualTo(1);

        verify(routeStopRepository).save(entity);
    }

    // -------------------------------------------------------------
    // SAVE error: same origin and destination
    // -------------------------------------------------------------
    @Test
    void shouldThrowWhenOriginEqualsDestinationOnSave() {
        var req = new RouteStopDTO.routeStopCreateRequest(
                1, 10L, 10L, 30L, null
        );

        assertThatThrownBy(() -> service.save(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("origen y destino no pueden ser iguales");
    }

    // -------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------
    @Test
    void shouldUpdateRouteStop() {
        var existing = RouteStop.builder()
                .id(8L)
                .stopOrder(1)
                .origin(null)
                .destination(null)
                .route(null)
                .build();

        when(routeStopRepository.findById(8L)).thenReturn(Optional.of(existing));

        var req = new RouteStopDTO.routeStopUpdateRequest(
                2, 10L, 20L, 30L, null
        );

        var origin = Stop.builder()
                .id(10L)
                .build();

        var destination = Stop.builder()
                .id(20L)
                .build();

        var route = Route.builder()
                .id(30L)
                .routeStops(new ArrayList<>())
                .trips(new ArrayList<>())
                .build();

        doAnswer(inv -> {
            var updateReq = (RouteStopDTO.routeStopUpdateRequest) inv.getArgument(0);
            var ent = (RouteStop) inv.getArgument(1);
            ent.setStopOrder(updateReq.stopOrder());
            return null;
        }).when(routeStopMapper).update(req, existing);

        when(stopService.getObject(10L)).thenReturn(origin);
        when(stopService.getObject(20L)).thenReturn(destination);
        when(routeService.getObject(30L)).thenReturn(route);

        when(routeStopRepository.save(existing)).thenReturn(existing);

        var dto = new RouteStopDTO.routeStopResponse(8L, 2, null, null, null, null);
        when(routeStopMapper.toDto(existing)).thenReturn(dto);

        var result = service.update(8L, req);

        assertThat(result.stopOrder()).isEqualTo(2);
    }

    // -------------------------------------------------------------
    // UPDATE error: same IDs
    // -------------------------------------------------------------
    @Test
    void shouldThrowWhenOriginEqualsDestinationOnUpdate() {
        var existing = RouteStop.builder().id(1L).build();
        when(routeStopRepository.findById(1L)).thenReturn(Optional.of(existing));

        var req = new RouteStopDTO.routeStopUpdateRequest(
                1, 10L, 10L, 30L, null
        );

        assertThatThrownBy(() -> service.update(1L, req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // -------------------------------------------------------------
    // GET
    // -------------------------------------------------------------
    @Test
    void shouldReturnRouteStop() {
        var entity = RouteStop.builder().id(55L).build();
        when(routeStopRepository.findById(55L)).thenReturn(Optional.of(entity));

        var dto = new RouteStopDTO.routeStopResponse(55L, 1, null, null, null, null);
        when(routeStopMapper.toDto(entity)).thenReturn(dto);

        var result = service.get(55L);

        assertThat(result.id()).isEqualTo(55L);
    }

    // -------------------------------------------------------------
    // GET ALL
    // -------------------------------------------------------------
    @Test
    void shouldReturnPagedRouteStops() {
        var entity = RouteStop.builder().id(7L).build();

        var page = new PageImpl<>(List.of(entity));
        when(routeStopRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);

        var dto = new RouteStopDTO.routeStopResponse(7L, 1, null, null, null, null);
        when(routeStopMapper.toDto(entity)).thenReturn(dto);

        var result = service.getAll(PageRequest.of(0, 5));

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    // -------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------
    @Test
    void shouldDeleteRouteStop() {
        service.delete(44L);
        verify(routeStopRepository).deleteById(44L);
    }

    // -------------------------------------------------------------
    // GET OBJECT (NOT FOUND)
    // -------------------------------------------------------------
    @Test
    void shouldThrowWhenRouteStopNotFound() {
        when(routeStopRepository.findById(500L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getObject(500L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("RouteStop not found");
    }
}
