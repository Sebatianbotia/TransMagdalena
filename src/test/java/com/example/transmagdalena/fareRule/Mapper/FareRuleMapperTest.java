package com.example.transmagdalena.fareRule.Mapper;

import com.example.transmagdalena.fareRule.DTO.FareRuleDTO;
import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.stop.Stop;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FareRuleMapperTest {
    private final FareRuleMapper fareRuleMapper = Mappers.getMapper(FareRuleMapper.class);

    @Test
    void testToEntity() {
        FareRuleDTO.fareRuleCreateRequest createRequest = new FareRuleDTO.fareRuleCreateRequest(1L, new BigDecimal("50000.00"), true);

        FareRule fareRule = fareRuleMapper.toEntity(createRequest);

        assertNotNull(fareRule);
        assertEquals(new BigDecimal("50000.00"), fareRule.getBasePrice());
        assertTrue(fareRule.isDynamicPricing());
    }

    @Test
    void testToDto() {
        Route route = Route.builder().id(1L).code("R001").build();

        FareRule fareRule = FareRule.builder().id(1L).route(route).basePrice(new BigDecimal("75000.00")).isDynamicPricing(false).build();

        FareRuleDTO.fareRuleResponse response = fareRuleMapper.toDto(fareRule);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertNotNull(response.route());
        assertEquals(new BigDecimal("75000.00"), response.basePrice());
        assertFalse(response.isDynamicPricing());
    }

    @Test
    void testToStopDTO() {
        Stop origin = Stop.builder().id(1L).name("Barranquilla").build();

        Stop destination = Stop.builder().id(2L).name("Cartagena").build();

        RouteStop routeStop = RouteStop.builder().id(1L).origin(origin).destination(destination).stopOrder(1).build();

        RouteStopDTO.stopDTO stopDTO = fareRuleMapper.toStopDTO(routeStop);

        assertNotNull(stopDTO);
    }

    @Test
    void testToRouteDTO() {
        Route route = Route.builder().id(1L).code("R001").build();

        RouteStopDTO.routeDTO routeDTO = fareRuleMapper.toRouteDTO(route);

        assertNotNull(routeDTO);
        assertEquals(1L, routeDTO.id());
        assertEquals("R001", routeDTO.code());
    }

    @Test
    void testToFareRuleDto() {
        FareRule fareRule = FareRule.builder().id(1L).basePrice(new BigDecimal("60000.00")).isDynamicPricing(true).build();

        RouteStopDTO.fareRuleDTO fareRuleDTO = fareRuleMapper.toFareRuleDto(fareRule);

        assertNotNull(fareRuleDTO);
    }

}