package com.example.transmagdalena.baggage.controller;

import com.example.transmagdalena.baggage.DTO.BaggageDTO;
import com.example.transmagdalena.baggage.service.BaggageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/v1/baggage")
@RequiredArgsConstructor
public class BaggageController {

    private final BaggageService service;

    @GetMapping("/{tagcode}")
    public ResponseEntity<BaggageDTO.baggageResponse> getById(@PathVariable String tagcode) {
        return ResponseEntity.ok(service.get(tagcode));
    }

    @PostMapping("/create")
    public ResponseEntity<BaggageDTO.baggageResponse> create(@RequestBody BaggageDTO.baggageCreateRequest baggageDTO, UriComponentsBuilder uriBuilder) {
        var s = service.save(baggageDTO);
        var location = uriBuilder.path("api/v1/baggage/{tagcode}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BaggageDTO.baggageResponse>> getAll(@RequestParam  Integer page, @RequestParam Integer size) {
        var s = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(service.getAll(s));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<BaggageDTO.baggageResponse> update(@PathVariable Long id,
                                                             @RequestBody BaggageDTO.baggageUpdateRequest request) {
        return ResponseEntity.ok(service.update(request, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaggageDTO.baggageResponse> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

 }
