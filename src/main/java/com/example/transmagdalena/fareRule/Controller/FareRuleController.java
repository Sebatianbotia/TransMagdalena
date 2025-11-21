package com.example.transmagdalena.fareRule.Controller;

import com.example.transmagdalena.fareRule.DTO.FareRuleDTO;
import com.example.transmagdalena.fareRule.Service.FareRuleService;
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
@RequestMapping("api/v1/fareRule")
@RequiredArgsConstructor
@Validated
public class FareRuleController {

    @Autowired
    private final FareRuleService fareRuleService;

    @GetMapping("/{id}")
    public ResponseEntity<FareRuleDTO.fareRuleResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(fareRuleService.get(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<FareRuleDTO.fareRuleResponse>> getAll(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "5") int size) {
        var p = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(fareRuleService.getAll(p));
    }


    @PostMapping("/create")
    public ResponseEntity<FareRuleDTO.fareRuleResponse> create(@RequestBody @Valid FareRuleDTO.fareRuleCreateRequest request,
                                                       UriComponentsBuilder uriBuilder) {
        var s =  fareRuleService.save(request);
        var location = uriBuilder.path("/api/v1/fareRule/{id}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<FareRuleDTO.fareRuleResponse> update(@Valid @RequestBody FareRuleDTO.fareRuleUpdateRequest request,
                                                       @PathVariable Long id){
        return ResponseEntity.ok(fareRuleService.update(request, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FareRuleDTO.fareRuleResponse> delete(@PathVariable Long id){
        fareRuleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
