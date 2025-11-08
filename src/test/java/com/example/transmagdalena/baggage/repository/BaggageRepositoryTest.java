package com.example.transmagdalena.baggage.repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.baggage.Baggage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class BaggageRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private BaggageRepository baggageRepository;

    @Test
    @DisplayName("buscar baggage por id")
    public void buscarBaggagePorId(){

        Baggage baggage = Baggage.builder().tagCode("ghhh").weight(22).build();
        baggage = baggageRepository.save(baggage);

        Optional<Baggage> optional = baggageRepository.findById(baggage.getId());
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(baggage.getId(), optional.get().getId());
        Assertions.assertEquals(baggage.getTagCode(), optional.get().getTagCode());

    }
}
