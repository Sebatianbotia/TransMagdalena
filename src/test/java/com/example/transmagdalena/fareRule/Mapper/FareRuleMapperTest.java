package com.example.transmagdalena.fareRule.Mapper;

import com.example.transmagdalena.fareRule.DTO.FareRuleDTO;
import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.stop.Stop;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FareRuleMapperTest {
    private final FareRuleMapper fareRuleMapper = Mappers.getMapper(FareRuleMapper.class);

    @Test
    void testToEntity() {
        // Given
        FareRuleDTO.fareRuleCreateRequest createRequest =
                new FareRuleDTO.fareRuleCreateRequest(1L, 2L, new BigDecimal("50000.00"), true);

        // When
        FareRule fareRule = fareRuleMapper.toEntity(createRequest);

        // Then
        assertNotNull(fareRule);
        assertEquals(new BigDecimal("50000.00"), fareRule.getBasePrice());
        assertTrue(fareRule.getIsDynamicPricing());
    }

    @Test
    void testToDto() {
        // Given
        Stop origin = Stop.builder()
                .id(1L)
                .name("Barranquilla")
                .lat(10.9876f)
                .lng(-74.7890f)
                .build();

        Stop destination = Stop.builder()
                .id(2L)
                .name("Cartagena")
                .lat(10.3910f)
                .lng(-75.4795f)
                .build();

        FareRule fareRule = FareRule.builder()
                .id(1L)
                .origin(origin)
                .destination(destination)
                .basePrice(new BigDecimal("75000.00"))
                .isDynamicPricing(false)
                .build();

        // When
        FareRuleDTO.fareRuleResponse response = fareRuleMapper.toDto(fareRule);

        // Then - Verificar el response completo con los stopDTO internos
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(new BigDecimal("75000.00"), response.basePrice());
        assertFalse(response.isDynamicPricing());

        // Verificar que los stopDTO se mapearon correctamente dentro del response
        assertNotNull(response.origin());
        assertEquals(1L, response.origin().id());
        assertEquals("Barranquilla", response.origin().name());
        assertEquals(10.9876f, response.origin().lat());
        assertEquals(-74.7890f, response.origin().lng());

        assertNotNull(response.destination());
        assertEquals(2L, response.destination().id());
        assertEquals("Cartagena", response.destination().name());
        assertEquals(10.3910f, response.destination().lat());
        assertEquals(-75.4795f, response.destination().lng());
    }


    @Test
    void testUpdate() {
        // Given
        FareRule fareRule = FareRule.builder()
                .id(1L)
                .basePrice(new BigDecimal("50000.00"))
                .isDynamicPricing(true)
                .build();

        FareRuleDTO.fareRuleUpdateRequest updateRequest =
                new FareRuleDTO.fareRuleUpdateRequest(3L, 4L, new BigDecimal("60000.00"), false);

        // When
        fareRuleMapper.update(updateRequest, fareRule);

        // Then
        assertEquals(new BigDecimal("60000.00"), fareRule.getBasePrice());
        assertFalse(fareRule.getIsDynamicPricing());
    }

    @Test
    void testUpdateWithNullValues() {
        // Given
        FareRule fareRule = FareRule.builder()
                .id(1L)
                .basePrice(new BigDecimal("50000.00"))
                .isDynamicPricing(true)
                .build();

        FareRuleDTO.fareRuleUpdateRequest updateRequest =
                new FareRuleDTO.fareRuleUpdateRequest(null, null, null, null);

        // When
        fareRuleMapper.update(updateRequest, fareRule);

        // Then - Los valores originales deben mantenerse por NullValuePropertyMappingStrategy.IGNORE
        assertEquals(new BigDecimal("50000.00"), fareRule.getBasePrice());
        assertTrue(fareRule.getIsDynamicPricing());
    }


}