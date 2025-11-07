package com.example.transmagdalena.ticket.repository;

import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.ticket.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findTicketsByUser_Id(Long userId, Pageable pageable);

    // Busca las stops de las rutas (se usara para calcular los precios de cada ticket)
    @Query("""
    Select rs from Ticket t
    join RouteStop rs on rs.route = t.trip.route
    where rs.stopOrder between (
    select f.stopOrder from RouteStop f
    where f.origin = t.origin
    ) AND (
    select ff.stopOrder from RouteStop ff
    where ff.destination = t.destination
    )
    and t.user.id = :userId and t.trip.id = :tripId
""")
    Set<RouteStop> findRouteStopsByUserId(@Param(value = "userId") Long userId,
                                          @Param(value = "tripId") Long tripId
                                          );
}
