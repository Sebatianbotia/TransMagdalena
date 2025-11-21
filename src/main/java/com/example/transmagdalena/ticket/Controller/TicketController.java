package com.example.transmagdalena.ticket.Controller;

import com.example.transmagdalena.assignment.DTO.AssignmentDTO;
import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.ticket.DTO.TicketDTO;
import com.example.transmagdalena.ticket.service.TicketService;
import com.example.transmagdalena.ticket.service.TicketServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/ticket")
@RequiredArgsConstructor
@Validated
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/create")
    public ResponseEntity<TicketDTO.ticketResponse> create(@RequestBody TicketDTO.ticketCreateRequest ticketDTO) {
        return ResponseEntity.ok(ticketService.create(ticketDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO.ticketResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.get(id));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TicketDTO.ticketResponse> update(@PathVariable Long id, @Valid @RequestBody TicketDTO.ticketUpdateRequest req) {
        return ResponseEntity.ok(ticketService.update(id,req));
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<TicketDTO.ticketResponse> cancel(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<TicketDTO.ticketResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        var p = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(ticketService.getAll(p));
    }
}
