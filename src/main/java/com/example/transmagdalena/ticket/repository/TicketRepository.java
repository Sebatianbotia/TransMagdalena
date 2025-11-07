package com.example.transmagdalena.ticket.repository;

import com.example.transmagdalena.ticket.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findTicketsByUser_Id(Long userId, Pageable pageable);
}
