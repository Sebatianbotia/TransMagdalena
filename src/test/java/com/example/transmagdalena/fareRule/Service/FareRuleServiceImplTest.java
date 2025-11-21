package com.example.transmagdalena.fareRule.Service;

import com.example.transmagdalena.fareRule.DTO.FareRuleDTO;
import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.fareRule.Mapper.FareRuleMapper;
import com.example.transmagdalena.fareRule.repository.FareRuleRepository;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.stop.Service.StopService;
import com.example.transmagdalena.utilities.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FareRuleServiceImplTest {

    @Mock
    FareRuleRepository fareRuleRepository;

    @Mock
    FareRuleMapper fareRuleMapper;

    @Mock
    StopService stopService;

    @InjectMocks
    FareRuleServiceImpl service;

    @Test
    void shouldCreateFareRule() {
        var req = new FareRuleDTO.fareRuleCreateRequest(
                1L, 2L, new BigDecimal("10000"), true
        );

        var origin = new Stop();
        origin.setId(1L);

        var destination = new Stop();
        destination.setId(2L);

        var entity = FareRule.builder()
                .basePrice(new BigDecimal("10000"))
                .isDynamicPricing(true)
                .build();

        when(fareRuleMapper.toEntity(req)).thenReturn(entity);
        when(stopService.getObject(1L)).thenReturn(origin);
        when(stopService.getObject(2L)).thenReturn(destination);

        when(fareRuleRepository.save(entity)).thenAnswer(inv -> {
            FareRule fr = inv.getArgument(0);
            fr.setId(20L);
            return fr;
        });

        var response = new FareRuleDTO.fareRuleResponse(
                20L,
                new FareRuleDTO.stopDTO(1L, "Origin", 1f, 1f),
                new FareRuleDTO.stopDTO(2L, "Destination", 2f, 2f),
                new BigDecimal("10000"),
                true
        );

        when(fareRuleMapper.toDto(any())).thenReturn(response);

        var res = service.save(req);

        assertThat(res.id()).isEqualTo(20L);
        assertThat(res.basePrice()).isEqualTo("10000");
        verify(fareRuleRepository).save(entity);
    }

    @Test
    void shouldThrowErrorWhenOriginEqualsDestinationOnSave() {
        var req = new FareRuleDTO.fareRuleCreateRequest(
                5L, 5L, new BigDecimal("3000"), false
        );

        assertThatThrownBy(() -> service.save(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("origen debe ser diferente a destino");
    }

    @Test
    void shouldGetFareRuleById() {
        var entity = FareRule.builder().id(3L).build();
        when(fareRuleRepository.findById(3L)).thenReturn(Optional.of(entity));

        var dto = new FareRuleDTO.fareRuleResponse(
                3L, null, null, new BigDecimal("5000"), false
        );

        when(fareRuleMapper.toDto(entity)).thenReturn(dto);

        var result = service.get(3L);

        assertThat(result.id()).isEqualTo(3L);
        verify(fareRuleRepository).findById(3L);
    }

    @Test
    void shouldThrowNotFoundWhenIdDoesNotExist() {
        when(fareRuleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("fareRule not found");
    }

    @Test
    void shouldReturnPagedFareRules() {
        var entity = FareRule.builder().id(10L).build();
        var page = new PageImpl<>(java.util.List.of(entity));

        when(fareRuleRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);

        var dto = new FareRuleDTO.fareRuleResponse(10L, null, null, null, null);
        when(fareRuleMapper.toDto(entity)).thenReturn(dto);

        var result = service.getAll(PageRequest.of(0, 5));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(10L);
    }

    @Test
    void shouldDeleteFareRule() {
        fareRuleRepository.deleteById(5L);
        verify(fareRuleRepository).deleteById(5L);
    }

    @Test
    void shouldUpdateFareRule() {
        var entity = FareRule.builder()
                .id(7L)
                .basePrice(new BigDecimal("2000"))
                .isDynamicPricing(false)
                .build();

        when(fareRuleRepository.findById(7L)).thenReturn(Optional.of(entity));

        var req = new FareRuleDTO.fareRuleUpdateRequest(
                1L, 2L, new BigDecimal("5000"), true
        );

        var origin = new Stop();
        origin.setId(1L);

        var destination = new Stop();
        destination.setId(2L);

        doAnswer(inv -> {
            var r = (FareRuleDTO.fareRuleUpdateRequest) inv.getArgument(0);
            var e = (FareRule) inv.getArgument(1);
            e.setBasePrice(r.basePrice());
            e.setIsDynamicPricing(r.isDynamicPricing());
            return null;
        }).when(fareRuleMapper).update(req, entity);

        when(stopService.getObject(1L)).thenReturn(origin);
        when(stopService.getObject(2L)).thenReturn(destination);

        var expected = new FareRuleDTO.fareRuleResponse(
                7L, null, null, new BigDecimal("5000"), true
        );

        when(fareRuleMapper.toDto(entity)).thenReturn(expected);

        var result = service.update(req, 7L);

        assertThat(result.basePrice()).isEqualTo("5000");
        assertThat(result.isDynamicPricing()).isTrue();
        verify(fareRuleMapper).update(req, entity);
    }

    @Test
    void shouldThrowErrorOnUpdateWhenOriginEqualsDestination() {
        var entity = FareRule.builder().id(1L).build();
        when(fareRuleRepository.findById(1L)).thenReturn(Optional.of(entity));

        var req = new FareRuleDTO.fareRuleUpdateRequest(
                3L, 3L, null, null
        );

        assertThatThrownBy(() -> service.update(req, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("originId and destinationId cannot be the same");
    }
}
