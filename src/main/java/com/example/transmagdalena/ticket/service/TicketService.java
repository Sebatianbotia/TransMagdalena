package com.example.transmagdalena.ticket.service;

import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.ticket.DTO.TicketDTO;
import com.example.transmagdalena.ticket.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketService {
    TicketDTO.ticketResponse create(TicketDTO.ticketCreateRequest request);
    TicketDTO.ticketResponse update(Long id, TicketDTO.ticketUpdateRequest request);
    void delete(Long id);
    Ticket getObject(long id);
    TicketDTO.ticketResponse get(Long id);
    Page<TicketDTO.ticketResponse> getAll(Pageable pageable);
    List<RouteStop> findRouteStopsByUserId(Long userId, Long tripId);
    Page<TicketDTO.ticketResponse> getUserTickets(Pageable pageable, Long id);

}