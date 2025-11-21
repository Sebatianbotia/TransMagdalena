package com.example.transmagdalena.routeStop.Mapper;

import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.stop.Stop;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RouteStopMapperTest {
    private final RouteStopMapper routeStopMapper = Mappers.getMapper(RouteStopMapper.class);

    @Test
    void testToEntity() {
        RouteStopDTO.fareRuleCreateRequest fareRuleRequest = new RouteStopDTO.fareRuleCreateRequest(new BigDecimal("50000.00"), true);
        RouteStopDTO.routeStopCreateRequest createRequest = new RouteStopDTO.routeStopCreateRequest(1, 1L, 2L, 1L, fareRuleRequest);

        RouteStop routeStop = routeStopMapper.toEntity(createRequest);

        assertNotNull(routeStop);
        assertEquals(1, routeStop.getStopOrder());
    }

    @Test
    void testToFareRuleEntity() {
        RouteStopDTO.fareRuleCreateRequest fareRuleRequest = new RouteStopDTO.fareRuleCreateRequest(new BigDecimal("75000.00"), false);

        FareRule fareRule = routeStopMapper.toFareRuleEntity(fareRuleRequest);

        assertNotNull(fareRule);
        assertEquals(new BigDecimal("75000.00"), fareRule.getBasePrice());
        assertFalse(fareRule.getIsDynamicPricing());
    }

    @Test
    void testToDto() {
        Stop origin = Stop.builder().id(1L).name("Barranquilla").lat(10.9639f).lng(-74.7964f).build();
        Stop destination = Stop.builder().id(2L).name("Cartagena").lat(10.3910f).lng(-75.4794f).build();
        Route route = Route.builder().id(1L).code("R001").origin(origin).destination(destination).build();
        FareRule fareRule = FareRule.builder().id(1L).basePrice(new BigDecimal("60000.00")).isDynamicPricing(true).build();
        RouteStop routeStop = RouteStop.builder().id(1L).stopOrder(1).route(route).origin(origin).destination(destination).fareRule(fareRule).build();

        RouteStopDTO.routeStopResponse response = routeStopMapper.toDto(routeStop);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(1, response.stopOrder());
        assertNotNull(response.route());
    }

    @Test
    void testToStopDTO() {
        Stop origin = Stop.builder().id(1L).name("Santa Marta").lat(11.2404f).lng(-74.2110f).build();

        RouteStopDTO.stopDTO stopDTO = routeStopMapper.toStopDTO(origin);

        assertNotNull(stopDTO);
        assertEquals(1L, stopDTO.id());
        assertEquals("Santa Marta", stopDTO.name());
    }

    @Test
    void testToRouteDTO() {
        Stop origin = Stop.builder().id(1L).name("Valledupar").lat(10.4631f).lng(-73.2532f).build();
        Stop destination = Stop.builder().id(2L).name("Riohacha").lat(11.5444f).lng(-72.9072f).build();
        Route route = Route.builder().id(1L).code("R999").origin(origin).destination(destination).build();

        RouteStopDTO.routeDTO routeDTO = routeStopMapper.toRouteDTO(route);

        assertNotNull(routeDTO);
        assertEquals(1L, routeDTO.id());
        assertEquals("R999", routeDTO.code());
        assertNotNull(routeDTO.origin());
        assertNotNull(routeDTO.destination());
    }

    @Test
    void testToFareRuleDto() {
        FareRule fareRule = FareRule.builder().id(1L).basePrice(new BigDecimal("90000.00")).isDynamicPricing(true).build();

        RouteStopDTO.fareRuleDTO fareRuleDTO = routeStopMapper.toFareRuleDto(fareRule);

        assertNotNull(fareRuleDTO);
        assertEquals(1L, fareRuleDTO.id());
        assertEquals(new BigDecimal("90000.00"), fareRuleDTO.basePrice());
        assertTrue(fareRuleDTO.isDynamicPricing());
    }
}