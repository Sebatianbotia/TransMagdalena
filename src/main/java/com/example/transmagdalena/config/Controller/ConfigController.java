package com.example.transmagdalena.config.Controller;

import com.example.transmagdalena.city.DTO.CityDTO;
import com.example.transmagdalena.config.DTO.ConfigDTO;
import com.example.transmagdalena.config.Service.ConfigService;
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
@RequestMapping("api/v1/config")
@Validated
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    @GetMapping("/{id}")
    public ResponseEntity<ConfigDTO.configResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(configService.get(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ConfigDTO.configResponse>> getAll(@RequestParam (defaultValue = "0") int page,
                                                                 @RequestParam (defaultValue = "10") int size) {
        var p = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(configService.getAll(p));
    }

//    @GetMapping("/name/{name}")
//    public ResponseEntity<ConfigDTO.configResponse> getByName(@PathVariable String name){
//        return ResponseEntity.ok(cityService.get(name));
//    }

    @PostMapping("/create")
    public ResponseEntity<ConfigDTO.configResponse> create(@RequestBody @Valid ConfigDTO.configCreateRequest request,
                                                       UriComponentsBuilder uriBuilder) {
        var s =  configService.save(request);
        var location = uriBuilder.path("/api/v1/config/{id}").buildAndExpand(s.id()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ConfigDTO.configResponse> update(@Valid @RequestBody ConfigDTO.configUpdateRequest request,
                                                       @PathVariable Long id){
        return ResponseEntity.ok(configService.update(id, request));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ConfigDTO.configResponse> delete(@PathVariable Long id){
        configService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
