package com.example.transmagdalena.incidents.Controller;

import com.example.transmagdalena.fareRule.DTO.FareRuleDTO;
import com.example.transmagdalena.incidents.DTO.IncidentDTO;
import com.example.transmagdalena.incidents.EntityType;
import com.example.transmagdalena.incidents.service.IncidentService;
import com.example.transmagdalena.incidents.service.IncidentServiceImpl;
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
@RequestMapping("api/v1/incident")
@RequiredArgsConstructor
@Validated
public class IncidentController {

    private final IncidentServiceImpl incidentService;

    @GetMapping("/find")
    public ResponseEntity<Page<IncidentDTO.incidentResponse>> getIncidentByEntityId(@RequestParam Long entityId,
                                                                                   @RequestParam EntityType entityType,
                                                                                   @RequestParam int page,
                                                                                   @RequestParam int size) {
        var s = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        return ResponseEntity.ok(incidentService.getIncidentByIdAndEntityType(entityId, entityType, s));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<IncidentDTO.incidentResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "5") int size) {
        var p = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(incidentService.getAll(p));
    }


    @PostMapping("/create")
    public ResponseEntity<IncidentDTO.incidentResponse> create(@RequestBody @Valid IncidentDTO.incidentCreateRequest request,
                                                               UriComponentsBuilder uriBuilder) {
        var s =  incidentService.save(request);
        var location = uriBuilder.path("/api/v1/incident/{id}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<IncidentDTO.incidentResponse> update(@Valid @RequestBody IncidentDTO.incidentUpdateRequest  request,
                                                               @PathVariable Long id){
        return ResponseEntity.ok(incidentService.update(request, id));
    }

}
