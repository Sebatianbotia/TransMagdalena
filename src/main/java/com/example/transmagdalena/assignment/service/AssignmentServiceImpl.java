package com.example.transmagdalena.assignment.service;

import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.assignment.DTO.AssignmentDTO;
import com.example.transmagdalena.assignment.mapper.AssignmentMapper;
import com.example.transmagdalena.assignment.repository.AssignmentRepository;
import com.example.transmagdalena.trip.repository.TripRepository;
import com.example.transmagdalena.trip.service.TripServiceImpl;
import com.example.transmagdalena.user.Service.UserService;
import com.example.transmagdalena.user.Service.UserServiceImpl;
import com.example.transmagdalena.user.UserRols;
import com.example.transmagdalena.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final AssignmentMapper assignmentMapper;
    private final UserServiceImpl userService;
    private final TripServiceImpl tripService;

    @Override
    @Transactional
    public AssignmentDTO.assignmentResponse save(AssignmentDTO.assignmentCreateRequest req) {
        Assignment assignment = assignmentMapper.toEntity(req);
        assignment.setDispatcher(userService.getObject(req.dispatcherId(), UserRols.DISPATCHER));
        assignment.setDriver(userService.getObject(req.driverId(), UserRols.DRIVER));
        assignment.setTrip(tripService.getObject(req.tripId()));
       return assignmentMapper.toAssignmentDTO(assignmentRepository.save(assignment));
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentDTO.assignmentResponse get(Long id) {
        return assignmentMapper.toAssignmentDTO(getObject(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssignmentDTO.assignmentResponse> getAll(Pageable pageable) {
        return assignmentRepository.findAll(pageable).map(assignmentMapper::toAssignmentDTO);
    }

    @Override
    public void delete(Long id) {
        assignmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AssignmentDTO.assignmentResponse update(Long id, AssignmentDTO.assignmentUpdateRequest request) {
        var assignment = getObject(id);
        assignmentMapper.updateEntity(request, assignment);
        if (request.driverId() != null){
            assignment.setDriver(userService.getObject(request.driverId(), UserRols.DRIVER));
        }
        if (request.tripId() != null){
            assignment.setTrip(tripService.getObject(request.tripId()));
        }
        if (request.dispatcherId() != null){
            assignment.setDispatcher(userService.getObject(request.dispatcherId(), UserRols.DISPATCHER));
        }
        return assignmentMapper.toAssignmentDTO(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public Assignment getObject(Long id){
        return assignmentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("assignment not found"));
    }

}
