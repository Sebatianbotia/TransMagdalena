package com.example.transmagdalena.seatHold.repository;

import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seatHold.SeatHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeatHoldRepository extends JpaRepository<SeatHold, Long> {

    @Query("""
    select sh from SeatHold sh
     where sh.seat.number = :seatNumber and sh.trip.id  = :tripId
 """)
    Optional<SeatHold> findSeatHoldByNumber(@Param("seatNumber") Integer seatNumber, @Param("tripId") Long tripId);
}
