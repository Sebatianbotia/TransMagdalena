package com.example.transmagdalena.assignment.service;

import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.assignment.DTO.AssignmentDTO;
import com.example.transmagdalena.assignment.mapper.AssignmentMapper;
import com.example.transmagdalena.assignment.repository.AssignmentRepository;
import com.example.transmagdalena.trip.repository.TripRepository;
import com.example.transmagdalena.trip.service.TripServiceImpl;
import com.example.transmagdalena.user.Service.UserService;
import com.example.transmagdalena.user.Service.UserServiceImpl;
import com.example.transmagdalena.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentServiceImpl implements AssignmentService {
    AssignmentRepository assignmentRepository;
    AssignmentMapper assignmentMapper;
    UserServiceImpl userService;
    TripServiceImpl tripService;

    @Override
    @Transactional(readOnly = true)
    public AssignmentDTO.assignmentResponse save(AssignmentDTO.assignmentCreateRequest req) {
        Assignment assignment = assignmentMapper.toEntity(req);
        var driver = userService.getUser(req.driverId());
        var dispatcher =  userService.getUser(req.dispatcherId());
        var trip = tripService.getTripEntity(req.tripId());

        assignment.setDriver(driver);
        driver.getDriverAssignments().add(assignment);

        assignment.setDispatcher(dispatcher);
        dispatcher.getDispatcherAssignments().add(assignment); //es necesario hacer el save o la bidireccionalidad se asigna automaticamente?
        assignment.setTrip(trip);
        trip.getDispatcherAssignments().add(assignment); //es necesario hacer el save o la bidireccionalidad se asigna automaticamente?

        assignmentRepository.save(assignment);
        return assignmentMapper.toAssignmentDTO(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentDTO.assignmentResponse get(Long id) {
        var assignment = assignmentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("assigment not found"));
        return assignmentMapper.toAssignmentDTO(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssignmentDTO.assignmentResponse> getAll(Integer page, Integer elementsPage) {
        PageRequest pageRequest1 = PageRequest.of(page, elementsPage);
        Page<Assignment> assignments = assignmentRepository.findAll(pageRequest1);
        return assignments.map(e ->  assignmentMapper.toAssignmentDTO(e));
    }

    @Override
    public void delete(Long id) {
        assignmentRepository.deleteById(id);
    }

    @Override
    public AssignmentDTO.assignmentResponse update(Long id, AssignmentDTO.assignmentUpdateRequest request) {
        var assignment =  assignmentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("assignment not found"));
        assignmentMapper.updateEntity(request, assignment);
        return assignmentMapper.toAssignmentDTO(assignment);
    }

}
