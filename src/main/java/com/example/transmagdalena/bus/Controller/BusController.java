package com.example.transmagdalena.bus.Controller;

import com.example.transmagdalena.baggage.DTO.BaggageDTO;
import com.example.transmagdalena.bus.DTO.BusDTO;
import com.example.transmagdalena.bus.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/v1/bus")
@RequiredArgsConstructor
@Validated
public class BusController {

    private final BusService service;

    @GetMapping("/{id}")
    public ResponseEntity<BusDTO.busResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping("/create")
    public ResponseEntity<BusDTO.busResponse> create(@RequestBody BusDTO.busCreateRequest request,
                                                     UriComponentsBuilder uriBuilder) {
        var s = service.save(request);
        var location = uriBuilder.path("api/v1/bus/{id}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BusDTO.busResponse>> getAll(@RequestParam(defaultValue = "0")  Integer page,
                                                           @RequestParam(defaultValue = "10") Integer size) {

        var s = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(service.getAll(s));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<BusDTO.busResponse> update(@PathVariable Long id,
                                                             @RequestBody BusDTO.busUpdateRequest request) {
        return ResponseEntity.ok(service.update(request, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BusDTO.busResponse> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.countBuses());
    }
    
}
