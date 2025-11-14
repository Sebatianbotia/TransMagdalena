package com.example.transmagdalena.route.controller;

import com.example.transmagdalena.parcel.DTO.ParcelDTO;
import com.example.transmagdalena.route.DTO.RouteDTO;
import com.example.transmagdalena.route.service.RouteService;
import com.example.transmagdalena.route.service.RouteServiceImpl;
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

//    @PostMapping("/create")
//    public ResponseEntity<ParcelDTO.parcelResponse> create(@RequestBody @Valid ParcelDTO.parcelCreateRequest parcelDTO,
//                                                           UriComponentsBuilder uriBuilder){
//        var s = parcelService.save(parcelDTO);
//        var location =  uriBuilder.path("/api/v1/parcel/{id)").buildAndExpand(s.id()).toUri();
//        return ResponseEntity.created(location).body(s);
//    }
//
//    @PatchMapping("update/{id}")
//    public ResponseEntity<ParcelDTO.parcelResponse> update(@PathVariable Long id,
//                                                           @Valid @RequestBody ParcelDTO.parcelUpdateRequest parcelUpdateRequest){
//        return ResponseEntity.ok(parcelService.update(parcelUpdateRequest,id));
//    }
}
