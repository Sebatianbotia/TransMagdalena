package com.example.transmagdalena.baggage.mapper;

import com.example.transmagdalena.baggage.Baggage;
import com.example.transmagdalena.baggage.DTO.BaggageDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BaggageMapperTest {

    private final BaggageMapper mapper = Mappers.getMapper(BaggageMapper.class);

        @Test
        void toDto() {
            Baggage baggage = Baggage.builder().id(1L).weight(12).tagCode("aaa").fee(new BigDecimal("120.3")).build();

            BaggageDTO.baggageResponse dto = mapper.toDto(baggage);

            assertNotNull(dto);
            assertEquals(dto.tagCode(), baggage.getTagCode());
        }

        @Test
        void toEntity(){

            BaggageDTO.baggageCreateRequest baggage = new BaggageDTO.baggageCreateRequest(12, "aaab", new  BigDecimal("120.3"));

            Baggage bag = mapper.toEntity(baggage);
            assertNotNull(bag);
            assertEquals(baggage.tagCode(), bag.getTagCode());


        }

        @Test
    void updateEntity(){
            Baggage baggage = Baggage.builder().id(1L).weight(12).tagCode("aaa").fee(new BigDecimal("120.3")).build();
            var update = new BaggageDTO.baggageUpdateRequest(13, "aaa", new BigDecimal("13.2"));
            mapper.updateEntity(update,baggage);

            assertNotNull(baggage);
            assertEquals(update.tagCode(), baggage.getTagCode());
            assertEquals(update.weight(), baggage.getWeight());
        }


}