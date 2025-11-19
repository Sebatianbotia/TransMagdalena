package com.example.transmagdalena.assignment.Controller;

import com.example.transmagdalena.assignment.DTO.AssignmentDTO;
import com.example.transmagdalena.assignment.service.AssignmentService;
import com.example.transmagdalena.assignment.service.AssignmentServiceImpl;
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
@RequestMapping("api/v1/assignment")
@RequiredArgsConstructor
@Validated
public class AssignmentController {

    private final AssignmentServiceImpl assignmentService;

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO.assignmentResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.get(id));
    }

    @PostMapping("/create")
    public ResponseEntity<AssignmentDTO.assignmentResponse> create(@Valid @RequestBody AssignmentDTO.assignmentCreateRequest req,
                                                       UriComponentsBuilder uribuilder) {
        var f = assignmentService.save(req);
        var uri = uribuilder.path("/api/v1/assignment/{id}").buildAndExpand(f.id()).toUri();
        return ResponseEntity.created(uri).body(f);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<AssignmentDTO.assignmentResponse>> getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        var p = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(assignmentService.getAll(p));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AssignmentDTO.assignmentResponse> delete(@PathVariable Long id) {
        assignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<AssignmentDTO.assignmentResponse> update(@PathVariable Long id, @Valid @RequestBody AssignmentDTO.assignmentUpdateRequest req) {
        return ResponseEntity.ok(assignmentService.update(id,req));
    }


}
