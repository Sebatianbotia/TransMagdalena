package com.example.transmagdalena.city.Controller;

import com.example.transmagdalena.city.DTO.CityDTO;
import com.example.transmagdalena.city.service.CityService;
import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.Service.StopService;
import com.example.transmagdalena.stop.Stop;
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

import java.util.List;

@RestController
@RequestMapping("api/v1/city")
@RequiredArgsConstructor
@Validated
public class CityController {

    private final CityService cityService;
    private final StopService stopService;

    @GetMapping("/{id}")
    public ResponseEntity<CityDTO.cityResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.get(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<CityDTO.cityResponse>> getAll(@RequestParam int page, @RequestParam int size) {
        var p = PageRequest.of(page, size, Sort.by("name").ascending());
        return ResponseEntity.ok(cityService.getAll(p));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CityDTO.cityResponse> getByName(@PathVariable String name){
        return ResponseEntity.ok(cityService.get(name));
    }

    @PostMapping("/create")
    public ResponseEntity<CityDTO.cityResponse> create(@RequestBody @Valid CityDTO.cityCreateRequest cityDTO,
                                                       UriComponentsBuilder uriBuilder) {
        var s =  cityService.save(cityDTO);
        var location = uriBuilder.path("/api/v1/city/{id}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<CityDTO.cityResponse> update(@Valid @RequestBody CityDTO.cityUpdateRequest cityUpdateRequest,
                                                       @PathVariable Long id){
        return ResponseEntity.ok(cityService.update(cityUpdateRequest, id));
    }

    @GetMapping("/{id}/stops")
    public ResponseEntity<List<StopDTO.stopResponse>> getStops(@PathVariable Long id){
        return ResponseEntity.ok(stopService.getStopsByCity(id));
    }

}
