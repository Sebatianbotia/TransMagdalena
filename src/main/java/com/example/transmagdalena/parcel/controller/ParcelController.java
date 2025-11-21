package com.example.transmagdalena.parcel.controller;

import com.example.transmagdalena.parcel.DTO.ParcelDTO;
import com.example.transmagdalena.parcel.service.ParcelService;
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
@RequestMapping("api/v1/parcel")
@RequiredArgsConstructor
@Validated
public class ParcelController {

    private final ParcelService parcelService;

    @GetMapping("/{id}")
    public ResponseEntity<ParcelDTO.parcelResponse> get(@PathVariable Long id){
        return ResponseEntity.ok(parcelService.get(id));
    }

    @GetMapping("/tagCode/{tagCode}")
    public ResponseEntity<ParcelDTO.parcelResponse> get(@PathVariable String tagCode){
        return ResponseEntity.ok(parcelService.get(tagCode));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ParcelDTO.parcelResponse>> getAll(@RequestParam (defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size){
        var s = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(parcelService.getAll(s));
    }

    @PostMapping("/create")
    public ResponseEntity<ParcelDTO.parcelResponse> create(@RequestBody @Valid ParcelDTO.parcelCreateRequest parcelDTO,
                                                           UriComponentsBuilder uriBuilder){
        var s = parcelService.save(parcelDTO);
        var location =  uriBuilder.path("/api/v1/parcel/{id}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @PatchMapping("update/{id}")
    public ResponseEntity<ParcelDTO.parcelResponse> update(@PathVariable Long id,
                                                           @Valid @RequestBody ParcelDTO.parcelUpdateRequest parcelUpdateRequest){
        return ResponseEntity.ok(parcelService.update(parcelUpdateRequest,id));
    }





}
