package com.example.transmagdalena.seat.repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.seat.Seat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;



class SeatRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private SeatRepository seatRepository;

    @Test
    @DisplayName("Buscar asiento por ID")
    void buscarPorId(){
        Seat seat = new Seat();
        seat.setNumber(777);

        Seat saveSeat =  seatRepository.save(seat);

        Optional<Seat> foundSeat = seatRepository.findById(saveSeat.getId());

        assertTrue(foundSeat.isPresent());
        Assertions.assertThat(foundSeat.get().getId()).isEqualTo(saveSeat.getId());
    }


}