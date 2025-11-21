package com.example.transmagdalena.bus.Service;

import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.bus.BusStatus;
import com.example.transmagdalena.bus.DTO.BusDTO;
import com.example.transmagdalena.bus.mapper.BusMapper;
import com.example.transmagdalena.bus.repository.BusRepository;
import com.example.transmagdalena.bus.service.BusServiceImpl;
import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seat.SeatType;
import com.example.transmagdalena.utilities.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusServiceImplTest {

    @Mock
    BusRepository busRepository;

    @Mock
    BusMapper busMapper;

    @InjectMocks
    BusServiceImpl service;

    @Test
    void shouldCreateBusAndReturnDto() {
        var seatsReq = Set.of(
                new BusDTO.seatCreateRequest(1, SeatType.PREFERENTIAL),
                new BusDTO.seatCreateRequest(2, SeatType.STANDARD)
        );

        var req = new BusDTO.busCreateRequest(
                "ABC123", 40, BusStatus.ACTIVE, seatsReq
        );

        var seatA = Seat.builder().id(1L).number(1).type(SeatType.PREFERENTIAL).build();
        var seatB = Seat.builder().id(2L).number(2).type(SeatType.STANDARD).build();

        var seatSet = new HashSet<Seat>();
        seatSet.add(seatA);
        seatSet.add(seatB);

        var entity = Bus.builder()
                .plate("ABC123")
                .capacity(40)
                .status(BusStatus.ACTIVE)
                .seats(seatSet)
                .build();

        when(busMapper.toEntity(req)).thenReturn(entity);

        when(busRepository.save(entity)).thenAnswer(inv -> {
            Bus b = inv.getArgument(0);
            b.setId(10L);
            return b;
        });

        var resDto = new BusDTO.busResponse(
                10L, "ABC123", 40, BusStatus.ACTIVE,
                Set.of(
                        new BusDTO.seatResponseDto(1, SeatType.PREFERENTIAL),
                        new BusDTO.seatResponseDto(2, SeatType.STANDARD)
                )
        );

        when(busMapper.toBusDTO(any())).thenReturn(resDto);

        var result = service.save(req);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.plate()).isEqualTo("ABC123");
        verify(busRepository).save(entity);
    }


    @Test
    void shouldGetBusById() {
        var entity = Bus.builder()
                .id(5L)
                .plate("XYZ999")
                .capacity(50)
                .status(BusStatus.MAINTENANCE)
                .build();

        when(busRepository.findBusById(5L)).thenReturn(Optional.of(entity));

        var expectedDto = new BusDTO.busResponse(
                5L, "XYZ999", 50, BusStatus.MAINTENANCE, Set.of()
        );

        when(busMapper.toBusDTO(entity)).thenReturn(expectedDto);

        var result = service.get(5L);

        assertThat(result.id()).isEqualTo(5L);
        assertThat(result.plate()).isEqualTo("XYZ999");
    }

    @Test
    void shouldThrowWhenBusNotFound() {
        when(busRepository.findBusById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Bus not found");
    }

    @Test
    void shouldReturnPagedBusResponses() {
        var entity = Bus.builder()
                .id(3L)
                .plate("TTT123")
                .capacity(30)
                .status(BusStatus.ACTIVE)
                .build();

        var page = new PageImpl<>(java.util.List.of(entity));

        when(busRepository.findAll(PageRequest.of(0, 5)))
                .thenReturn(page);

        when(busMapper.toBusDTO(entity))
                .thenReturn(new BusDTO.busResponse(
                        3L, "TTT123", 30, BusStatus.ACTIVE, Set.of()
                ));

        var result = service.getAll(PageRequest.of(0, 5));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).plate()).isEqualTo("TTT123");
    }

    @Test
    void shouldDeleteBus() {
        var entity = Bus.builder()
                .id(8L)
                .plate("DEL999")
                .build();

        when(busRepository.findBusById(8L))
                .thenReturn(Optional.of(entity));

        service.delete(8L);

        verify(busRepository).delete(entity);
    }

    @Test
    void shouldReturnCountOfBuses() {
        when(busRepository.count()).thenReturn(15L);

        var count = service.countBuses();

        assertThat(count).isEqualTo(15L);
        verify(busRepository).count();
    }
}
