package com.example.transmagdalena.trip.repository;

import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.trip.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("""
select distinct t from Trip t
where exists (
select rs.id from RouteStop rs
join RouteStop rs2 on rs2.route = rs.route
where rs.origin.id = :origin and rs2.destination.id = :destination
and rs2.stopOrder >= rs.stopOrder
) and t.bus.capacity >= (select count(*) from SeatHold sh where sh.trip = t)

""")
    Page<Trip> findAllTripsBetweenOriginAndDestination(@Param("origin") Long originId, @Param("destination") Long destinationId,  Pageable pageable);

    @Query("""
    select sh.seat.number from SeatHold sh
    where sh.trip.id = :tripId
""")
    List<Integer> findSeatHolds(@Param("tripId")  Long tripId);

    Page<Trip> findTripsByBus_Id(Pageable pageable, Long bus_id);

    @Query("""
     select sh from SeatHold sh
     where sh.trip.id = :tripId and sh.status = 1
""")
    List<SeatHold> findUnpaidSeatHolds(@Param("tripId") Long routeId);
}
