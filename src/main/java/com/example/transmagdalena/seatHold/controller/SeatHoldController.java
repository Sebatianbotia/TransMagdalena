package com.example.transmagdalena.seatHold.controller;

import com.example.transmagdalena.seatHold.DTO.SeatHoldDTO;
import com.example.transmagdalena.seatHold.service.SeatHoldImpl;
import com.example.transmagdalena.seatHold.service.SeatHoldService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/v1/seatHold")
@RequiredArgsConstructor
@Validated
public class SeatHoldController {

    private final SeatHoldImpl seatHoldService;

    @GetMapping("/{id}")
    public ResponseEntity<SeatHoldDTO.seatHoldResponse> get(@PathVariable Long id){
        return ResponseEntity.ok(seatHoldService.get(id));
    }

    @GetMapping()
    public ResponseEntity<Page<SeatHoldDTO.seatHoldResponse>> getAll(@RequestParam int page,
                                                                     @RequestParam int size){
        var s = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(seatHoldService.getAll(s));
    }

    @PostMapping("/create")
    public ResponseEntity<SeatHoldDTO.seatHoldResponse> create(@Valid @RequestBody SeatHoldDTO.seatHoldCreateRequest seatHoldDTO,
                                                               UriComponentsBuilder uribuilder){
        var s = seatHoldService.save(seatHoldDTO);
        var location = uribuilder.path("api/v1/seatHold/{id}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<SeatHoldDTO.seatHoldResponse> update(@PathVariable Long id,
                                                               @Valid @RequestBody SeatHoldDTO.seatHoldUpdateRequest request){
        return ResponseEntity.ok(seatHoldService.update(request, id));
    }


}
