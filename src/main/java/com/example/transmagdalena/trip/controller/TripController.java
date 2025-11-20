package com.example.transmagdalena.trip.controller;

import com.example.transmagdalena.assignment.DTO.AssignmentDTO;
import com.example.transmagdalena.incidents.DTO.IncidentDTO;
import com.example.transmagdalena.incidents.EntityType;
import com.example.transmagdalena.incidents.service.IncidentServiceImpl;
import com.example.transmagdalena.seatHold.service.SeatHoldService;
import com.example.transmagdalena.ticket.DTO.TicketDTO;
import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Mapper.TripMapper;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.repository.TripRepository;
import com.example.transmagdalena.trip.service.TripService;
import com.example.transmagdalena.trip.service.TripServiceImpl;
import com.example.transmagdalena.user.UserRols;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/trip")
@RequiredArgsConstructor
@Validated
public class TripController {

    private final TripServiceImpl tripService;
    private final IncidentServiceImpl  incidentService;

    @GetMapping("/{id}")
    public ResponseEntity<TripDTO.tripResponse> getTrip(@PathVariable Long id){
        return ResponseEntity.ok(tripService.get(id));
    }

    @GetMapping("/{id}/incidents")
    public ResponseEntity<Page<IncidentDTO.incidentResponse>> getIncidents(@PathVariable Long id){
        var s = PageRequest.of(0,2);
        return ResponseEntity.ok(incidentService.getIncidentByIdAndEntityType(id, EntityType.TRIP, s));
    }

//    @GetMapping("/{id}/freeSeats")
//    public ResponseEntity<TripDTO.tripResponseWithSeatAvailable> getTripWithFreeSeats(@PathVariable Long id){
//        return ResponseEntity.ok(tripService.getTripWithSeatFree(id));
//    }

    @GetMapping("/{id}/freeSeats")
    public ResponseEntity<List<Integer>> getBusySeatsNumbers(@PathVariable Long id,
                                                             @RequestParam Long origin){
        return ResponseEntity.ok(tripService.getBusySeats(id, origin));
    }


    @GetMapping("/find")
    public ResponseEntity<Page<TripDTO.tripResponseWithSeatAvailable>> findTrips(@RequestParam Long origin,
                                                @RequestParam Long destination,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam UserRols userRols, @RequestParam LocalDate date) {
        var s = PageRequest.of(page, size);
        return ResponseEntity.ok(tripService.findTripsBetweenStops(origin, destination, s, userRols,  date));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<TripDTO.tripResponse>> getAll(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        var p = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(tripService.getAll(p));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TripDTO.tripResponse> update(@PathVariable Long id, @Valid @RequestBody TripDTO.tripUpdateRequest req) {
        return ResponseEntity.ok(tripService.update(req, id));
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<TripDTO.tripResponse> cancel(@PathVariable Long id) {
        tripService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    public ResponseEntity<TripDTO.tripResponse> create(@Valid @RequestBody TripDTO.tripCreateRequest req) {
        return ResponseEntity.ok(tripService.save(req));
    }




}
