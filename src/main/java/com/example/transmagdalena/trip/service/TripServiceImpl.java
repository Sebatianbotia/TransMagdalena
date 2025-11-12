package com.example.transmagdalena.trip.service;

import com.example.transmagdalena.bus.service.BusService;
import com.example.transmagdalena.bus.service.BusServiceImpl;
import com.example.transmagdalena.fareRule.Service.FareRuleService;
import com.example.transmagdalena.fareRule.Service.FareRuleServiceImpl;
import com.example.transmagdalena.route.service.RouteService;
import com.example.transmagdalena.route.service.RouteServiceImpl;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.seatHold.SeatHoldStatus;
import com.example.transmagdalena.seatHold.service.SeatHoldImpl;
import com.example.transmagdalena.seatHold.service.SeatHoldService;
import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Mapper.TripMapper;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.TripStatus;
import com.example.transmagdalena.trip.repository.TripRepository;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final BusServiceImpl busService;
    private final FareRuleServiceImpl fareRuleService;
    private final RouteServiceImpl routeService;


    @Override
    public TripDTO.tripResponse save(TripDTO.tripCreateRequest req) {
        if (req.departureAt().isAfter(req.arrivalAt())) {
            throw new IllegalArgumentException("la salida no puede ser antes de la llegada");
        }

//        if (req.departureAt().isBefore(OffsetDateTime.now())) {
//            throw new IllegalArgumentException("Departure time no pude ser en el pasado");
//        }

        var trip = tripMapper.toEntity(req);
        var bus = busService.getObject(req.busId());
        var route = routeService.getObject(req.routeId());
        var fareRule = fareRuleService.getObject(req.fareRuleId());

        trip.addRoute(route);
        trip.addBus(bus);
        trip.addFareRule(fareRule);
        var saved = tripRepository.save(trip);
        return tripMapper.tripResponse(saved);
    }

    @Override
    public TripDTO.tripResponse update(TripDTO.tripUpdateRequest request, Long tripId) {
        var trip = getObject(tripId);

        if(trip.getTripStatus().equals(TripStatus.ARRIVED) && request.tripStatus().equals(TripStatus.CANCELLED)){
            throw new IllegalArgumentException("No se puede cancelar un viaje finalizado");
        }
        if(trip.getTripStatus().equals(TripStatus.DEPARTED) && request.tripStatus().equals(TripStatus.CANCELLED)){
            throw new IllegalArgumentException("No se puede cancelar un viaje que ya salio");
        }

        tripMapper.update(request, trip);

        if(request.routeId()!=null){
            trip.addRoute(routeService.getObject(request.routeId()));
        }

        if(request.busId()!=null){
            trip.addBus(busService.getObject(request.busId()));
        }

        if(request.fareRuleId() != null){
            trip.addFareRule(fareRuleService.getObject(request.fareRuleId()));
        }


        return tripMapper.tripResponse(tripRepository.save(trip));
    }

    @Override
    public void delete(Long tripId) {
        tripRepository.deleteById(tripId);
    }

    @Override
    public TripDTO.tripResponse get(Long id) {
        return tripMapper.tripResponse(getObject(id));
    }

    @Override
    public Page<TripDTO.tripResponse> getAll(Pageable pageable) {
        return  tripRepository.findAll(pageable).map(tripMapper::tripResponse);
    }

    @Override
    public Trip getObject(Long id) {
        return tripRepository.findById(id).orElseThrow(() -> new NotFoundException("Trip not found"));
    }

    @Override
    public Page<TripDTO.tripResponse> getTripsByOriginAndDestination(Pageable pageable, String origin, String destination) {
        return tripRepository.findAllTripsBetweenOriginAndDestination(origin, destination, pageable).map(tripMapper::tripResponse);
    }

    @Override
    public List<Integer> findSeatsHold(Long tripId) {
        return tripRepository.findSeatHolds(tripId);
    }

    @Override
    public List<SeatHold> findUnpaidSeatsHold(Long tripId) {
        return tripRepository.findUnpaidSeatHolds(tripId);
    }

    public TripDTO.tripResponseWithSeatAvailable getTripWithSeatAvailable(Long tripId) {
        var trip = getObject(tripId);
        Integer seats = trip.getSeatHolds().stream().filter(
                f -> f.getStatus() == SeatHoldStatus.HOLD
        ).toList().size();
        seats = trip.getBus().getCapacity() - seats;
        return new TripDTO.tripResponseWithSeatAvailable(tripMapper.tripResponse(trip), seats);
    }


}
