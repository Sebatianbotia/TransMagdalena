package com.example.transmagdalena.seat.controller;

import com.example.transmagdalena.seat.DTO.SeatDTO;
import com.example.transmagdalena.seat.service.SeatServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/v1/seat")
@RequiredArgsConstructor
public class SeatController {

    @Autowired
    private final SeatServiceImpl seatService;

    @GetMapping("/{id}")
    public ResponseEntity<SeatDTO.seatResponse> getSeat(@PathVariable Long id) {
        return ResponseEntity.ok(seatService.get(id));
    }

    @PostMapping("/create")
    public ResponseEntity<SeatDTO.seatResponse> create(@Valid @RequestBody SeatDTO.seatCreateRequest seatDTO,
                                                       UriComponentsBuilder uriBuilder) {
        var s = seatService.save(seatDTO);
        var uri = uriBuilder.path("api/v1/seat/{id}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(uri).body(s);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<SeatDTO.seatResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody SeatDTO.seatUpdateRequest seatUpdateRequest) {
        return ResponseEntity.ok(seatService.update(seatUpdateRequest, id));
    }


}
