package com.example.transmagdalena.ticket.Controller;

import com.example.transmagdalena.ticket.DTO.TicketDTO;
import com.example.transmagdalena.ticket.service.TicketService;
import com.example.transmagdalena.ticket.service.TicketServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/ticket")
@RequiredArgsConstructor
@Validated
public class TicketController {

    private final TicketServiceImpl ticketService;

    @PostMapping("/create")
    public ResponseEntity<TicketDTO.ticketResponse> create(@RequestBody TicketDTO.ticketCreateRequest ticketDTO) {
        return ResponseEntity.ok(ticketService.create(ticketDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO.ticketResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.get(id));
    }
}
