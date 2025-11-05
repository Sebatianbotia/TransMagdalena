package com.example.transmagdalena.trip.repository;

import com.example.transmagdalena.trip.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("""
select distinct t from Trip t 
where exists (
select rs.id from RouteStop rs
join RouteStop rs2 on rs2.route = rs.route 
where rs.origin = :origin and rs2.destination = :destination
and rs2.stopOrder >= rs.stopOrder
) and t.bus.capacity >= (select count(*) from SeatHold sh where sh.trip = t)

""")
    Page<Trip> findAllTripsBetweenOriginAndDestination(@Param("origin") String origin, @Param("destination") String destination,  Pageable pageable);
}
