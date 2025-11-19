package com.example.transmagdalena.seat.controller;

import com.example.transmagdalena.parcel.DTO.ParcelDTO;
import com.example.transmagdalena.seat.DTO.SeatDTO;
import com.example.transmagdalena.seat.service.SeatServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/all")
    public ResponseEntity<Page<SeatDTO.seatResponse>> getAll(@RequestParam (defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size){
        var s = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(seatService.getAll(s));
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SeatDTO.seatResponse> delete(@PathVariable Long id) {
        seatService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
