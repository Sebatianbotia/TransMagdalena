package com.example.transmagdalena.trip.repository;

import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.trip.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("""
select distinct t from Trip t
where exists (
select rs.id d from RouteStop rs
join RouteStop rs2 on rs2.route = rs.route
where rs.origin.id = :origin and rs2.destination.id = :destination
and rs2.stopOrder >= rs.stopOrder and t.route = rs.route
) and t.date >= :date
""")
    Page<Trip> findAllTripsBetweenOriginAndDestination(@Param("origin") Long originId, @Param("destination") Long destinationId,
                                                       @Param("date") LocalDate date, Pageable pageable);

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

    // Busca las stops de las rutas (se usara para calcular los precios de cada ticket)
    @Query("""
    select rs from RouteStop rs
    join Trip t on t.route = rs.route
    where rs.stopOrder between (
    select f.stopOrder from RouteStop f
    where f.origin.id = :origin and f.route = t.route
    ) AND (
    select ff.stopOrder from RouteStop ff
    where ff.destination.id = :destination and ff.route = t.route
    )
    and t.id = :tripId
""")
    List<RouteStop> findRouteStopsByUserId(@Param("origin") Long origin,
                                           @Param("destination") Long destination,
                                           @Param(value = "tripId") Long tripId);


    @Query("""
    select t.seatHold.seat.number from RouteStop rs
    join Ticket t on rs.route = t.trip.route and t.trip.id = :trip
    where rs.destination.id = t.destination.id
    and rs.stopOrder >= (
    select rs2.stopOrder from RouteStop rs2
        where rs2.origin.id = :origin and rs2.route = t.trip.route
    )
""")
    List<Integer> findBusySeats(@Param("trip") Long tripId,
                                @Param("origin") Long originId);
}
