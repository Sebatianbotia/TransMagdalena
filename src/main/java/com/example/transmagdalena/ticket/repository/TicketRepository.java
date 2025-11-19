package com.example.transmagdalena.ticket.repository;

import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.ticket.Ticket;
import com.example.transmagdalena.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findTicketsByUser_Id(Long userId, Pageable pageable);

    Long user(User user);
}
