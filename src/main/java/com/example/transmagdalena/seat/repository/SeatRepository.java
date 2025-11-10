package com.example.transmagdalena.seat.repository;

import com.example.transmagdalena.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findById(Long id);
    Optional<Seat> findByNumberAndBusId(Integer number, Long busId);
    Integer countByBusId(Long busId);
}
