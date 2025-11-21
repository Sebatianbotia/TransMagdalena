package com.example.transmagdalena.routeStop.Controller;

import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.service.RouteStopService;
import com.example.transmagdalena.stop.DTO.StopDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/v1/routeStop")
@RequiredArgsConstructor
@Validated
public class RouteStopController {

    private final RouteStopService routeStopService;

    @GetMapping("/{id}")
    public ResponseEntity<RouteStopDTO.routeStopResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(routeStopService.get(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<RouteStopDTO.routeStopResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        var p = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(routeStopService.getAll(p));
    }

    @PostMapping("/create")
    public ResponseEntity<RouteStopDTO.routeStopResponse> create(@Valid @RequestBody RouteStopDTO.routeStopCreateRequest request,
                                                       UriComponentsBuilder uriBuilder) {
        var s = routeStopService.save(request);
        var location =  uriBuilder.path("/api/v1/stop/{id}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<RouteStopDTO.routeStopResponse> update(@PathVariable Long id,
                                                                 @Valid @RequestBody RouteStopDTO.routeStopUpdateRequest req) {
        return ResponseEntity.ok(routeStopService.update(id, req));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RouteStopDTO.routeStopResponse> delete(@PathVariable Long id) {
        routeStopService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
