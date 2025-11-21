package com.example.transmagdalena.stop.Controller;

import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.Service.StopService;
import com.example.transmagdalena.stop.Service.StopServiceImpl;
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
@RequestMapping("api/v1/stop")
@RequiredArgsConstructor
@Validated
public class StopController {

    private final StopServiceImpl stopService;

    @GetMapping("/{id}")
    public ResponseEntity<StopDTO.stopResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(stopService.get(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<StopDTO.stopResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        var p = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(stopService.getAll(p));
    }

    @PostMapping("/create")
    public ResponseEntity<StopDTO.stopResponse> create(@Valid @RequestBody StopDTO.stopCreateRequest stopDTO,
                                                       UriComponentsBuilder uriBuilder) {
        var s = stopService.save(stopDTO);
        var location =  uriBuilder.path("/api/v1/stop/{id}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<StopDTO.stopResponse> update(@PathVariable Long id, @Valid @RequestBody StopDTO.stopUpdateRequest stopDTO) {
        return ResponseEntity.ok(stopService.updateStop(stopDTO, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StopDTO.stopResponse> delete(@PathVariable Long id) {
        stopService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<StopDTO.stopResponse>> getStopsList() {
        return ResponseEntity.ok(stopService.getStops());
    }



}
