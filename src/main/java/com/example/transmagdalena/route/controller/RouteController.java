package com.example.transmagdalena.route.controller;

import com.example.transmagdalena.parcel.DTO.ParcelDTO;
import com.example.transmagdalena.route.DTO.RouteDTO;
import com.example.transmagdalena.route.service.RouteService;
import com.example.transmagdalena.route.service.RouteServiceImpl;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/v1/route")
@RequiredArgsConstructor
public class RouteController {

    private final RouteServiceImpl routeService;

    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO.routeResponse> get(@PathVariable Long id){
        return ResponseEntity.ok(routeService.get(id));
    }

    @GetMapping("/{id}/stops")
    public ResponseEntity<RouteDTO.routeResponseStops> getRouteStops(@PathVariable Long id){
        return ResponseEntity.ok(routeService.getRouteStops(id));
    }


    @GetMapping("/all")
    public ResponseEntity<Page<RouteDTO.routeResponse>> getAll(@RequestParam int page,
                                                                 @RequestParam int size){
        var s = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(routeService.getAll(s));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(){
        return ResponseEntity.ok(routeService.count());
    }

    @PostMapping("/create")
    public ResponseEntity<RouteDTO.routeResponse> create(@RequestBody @Valid RouteDTO.routeCreateRequest req,
                                                           UriComponentsBuilder uriBuilder){
        var s = routeService.save(req);
        var location =  uriBuilder.path("/api/v1/route/{id}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @PatchMapping("update/{id}")
    public ResponseEntity<RouteDTO.routeResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody RouteDTO.routeUpdateRequest req){
        return ResponseEntity.ok(routeService.update(id, req));
    }


}
