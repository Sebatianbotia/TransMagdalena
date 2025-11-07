package com.example.transmagdalena.seatHold.repository;

import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seatHold.SeatHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeatHoldRepository extends JpaRepository<SeatHold, Long> {

    @Query("""
    select s from Seat s 
     where s.number = :seatNumber and s.bus.id = :busId
 """)
    Optional<Seat> findSeatByNumber(@Param("busId") Long tripId, @Param("seatNumber") Integer seatNumber);
}
