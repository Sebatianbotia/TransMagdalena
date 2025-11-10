package com.example.transmagdalena.assignment.service;

import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.assignment.DTO.AssignmentDTO;
import com.example.transmagdalena.baggage.DTO.BaggageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface AssignmentService {
    AssignmentDTO.assignmentResponse save(AssignmentDTO.assignmentCreateRequest assignmentDTO);
    AssignmentDTO.assignmentResponse get(Long id);
    Page<AssignmentDTO.assignmentResponse> getAll(Pageable pageable);
    void delete(Long id);
    AssignmentDTO.assignmentResponse update(Long id, AssignmentDTO.assignmentUpdateRequest request);
    Assignment getObject(Long id);
}
