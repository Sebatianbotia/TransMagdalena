package com.example.transmagdalena.fareRule.Repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.fareRule.repository.FareRuleRepository;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.stop.repository.StopRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

public class FareRuleRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private FareRuleRepository fareRuleRepository;

    @Autowired
    private StopRepository stopRepository;

    @Test
    @DisplayName("buscar fareRule por id existente")
    void buscarFareRulePorIdExistente() {
        // Given
        Stop origin = Stop.builder().name("Barranquilla").lat(10.9876f).lng(-74.7890f).build();
        Stop destination = Stop.builder().name("Cartagena").lat(10.3910f).lng(-75.4795f).build();

        origin = stopRepository.save(origin);
        destination = stopRepository.save(destination);

        FareRule fareRule = FareRule.builder()
                .basePrice(new BigDecimal("50000.00"))
                .isDynamicPricing(true)
                .origin(origin)
                .destination(destination)
                .build();

        fareRule = fareRuleRepository.save(fareRule);

        // When
        Optional<FareRule> optional = fareRuleRepository.findById(fareRule.getId());

        // Then
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(fareRule.getId(), optional.get().getId());
        Assertions.assertEquals(fareRule.getBasePrice(), optional.get().getBasePrice());
        Assertions.assertEquals(fareRule.getIsDynamicPricing(), optional.get().getIsDynamicPricing());
        Assertions.assertEquals(fareRule.getOrigin().getId(), optional.get().getOrigin().getId());
        Assertions.assertEquals(fareRule.getDestination().getId(), optional.get().getDestination().getId());
    }


    @Test
    @DisplayName("buscar fareRule por id eliminado")
    void buscarFareRulePorIdEliminado() {
        // Given
        Stop origin = Stop.builder().name("Barranquilla").lat(10.9876f).lng(-74.7890f).build();
        Stop destination = Stop.builder().name("Santa Marta").lat(11.2408f).lng(-74.1990f).build();

        origin = stopRepository.save(origin);
        destination = stopRepository.save(destination);

        FareRule fareRule = FareRule.builder()
                .basePrice(new BigDecimal("45000.00"))
                .isDynamicPricing(false)
                .origin(origin)
                .destination(destination)
                .isDelete(true) // Marcado como eliminado
                .build();

        fareRule = fareRuleRepository.save(fareRule);

        // When
        Optional<FareRule> optional = fareRuleRepository.findById(fareRule.getId());

        // Then - No debe encontrarlo porque está marcado como eliminado (WHERE is_delete = false)
        Assertions.assertFalse(!optional.isPresent());
    }

}