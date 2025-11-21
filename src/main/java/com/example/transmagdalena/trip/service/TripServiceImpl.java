package com.example.transmagdalena.trip.service;

import com.example.transmagdalena.bus.service.BusService;
import com.example.transmagdalena.bus.service.BusServiceImpl;
import com.example.transmagdalena.config.Service.ConfigService;
import com.example.transmagdalena.fareRule.Service.FareRuleService;
import com.example.transmagdalena.fareRule.Service.FareRuleServiceImpl;
import com.example.transmagdalena.route.service.RouteService;
import com.example.transmagdalena.route.service.RouteServiceImpl;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.seat.DTO.SeatDTO;
import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seat.mapper.SeatMapper;
import com.example.transmagdalena.seat.service.SeatService;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.seatHold.SeatHoldStatus;
import com.example.transmagdalena.seatHold.service.SeatHoldImpl;
import com.example.transmagdalena.seatHold.service.SeatHoldService;
import com.example.transmagdalena.ticket.repository.TicketRepository;
import com.example.transmagdalena.ticket.service.TicketServiceImpl;
import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Mapper.TripMapper;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.TripStatus;
import com.example.transmagdalena.trip.repository.TripRepository;
import com.example.transmagdalena.tripQR.repository.TripQrRepository;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import com.example.transmagdalena.utilities.error.NotFoundException;
import com.example.transmagdalena.weather.Service.WeatherService;
import com.example.transmagdalena.weather.Service.WeatherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final BusService busService;
    private final FareRuleService fareRuleService;
    private final RouteService routeService;
    private final ConfigService configService;
    private final WeatherService weatherService;
    private final SeatService seatService;
    private final SeatMapper seatMapper;


    @Override
    public TripDTO.tripResponse save(TripDTO.tripCreateRequest req) {
        if (req.departureAt().isAfter(req.arrivalAt())) {
            throw new IllegalArgumentException("la salida no puede ser antes de la llegada");
        }

        if (req.departureAt().isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("Departure time no pude ser en el pasado");
        }

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
        var update = new TripDTO.tripUpdateRequest(null,null,null,null,null,TripStatus.CANCELLED, null);
        update(update,tripId);
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
    public List<Integer> getBusySeats(Long tripId, Long origin){
        return tripRepository.findBusySeats(tripId, origin);
    }

    @Override
    public Trip getObject(Long id) {
        return tripRepository.findById(id).orElseThrow(() -> new NotFoundException("Trip not found"));
    }


    @Override
    public List<Integer> findSeatsHold(Long tripId) {
        return tripRepository.findSeatHolds(tripId);
    }

    @Override
    public List<SeatDTO.seatResponse> getTripSeats(Long tripId) {
        var s = getObject(tripId);
        return s.getBus().getSeats().stream().map(seatMapper::toSeatResponse).toList();
    }


    @Override
    public List<SeatHold> findUnpaidSeatsHold(Long tripId) {
        return tripRepository.findUnpaidSeatHolds(tripId);
    }


    public int calculateFreeSeats(Trip trip, Long origin) {
        var busySeats = tripRepository.findBusySeats(trip.getId(), origin);
        return trip.getBus().getCapacity() - busySeats.size();

    }

    public Page<TripDTO.tripResponseWithSeatAvailable> findTripsBetweenStops(Long origin, Long destination,
                                                                             Pageable pageable, UserRols userRols, LocalDate date) {
        Page<Trip> foundtrips = tripRepository.findAllTripsBetweenOriginAndDestination(origin, destination, date, pageable);

        List<TripDTO.tripResponseWithSeatAvailable> filterTrips = foundtrips.stream().filter(
                f -> calculateFreeSeats(f, origin) > 0
        ).map(f -> {
            BigDecimal price = calculatePrice(f, origin, destination, userRols);
            return tripMapper.tripResponseWithAvailableSeat(f, calculateFreeSeats(f, origin), price.setScale(2,  BigDecimal.ROUND_HALF_UP));
        }).toList();

        return new PageImpl<>(filterTrips, pageable, foundtrips.getTotalElements());

    }

    public BigDecimal calculatePrice(Trip trip, Long origin, Long destination, UserRols userRols) {
        BigDecimal price;
        if (trip.getRoute().getOrigin().getId().equals(origin) && trip.getRoute().getDestination().getId().equals(destination)) {
            price = trip.getFareRule().getBasePrice();
            if (trip.getFareRule().getIsDynamicPricing()){
                var city = trip.getRoute().getOrigin().getCity().getId();
                var weatherDiscount = weatherService.get(trip.getDate(), trip.getDepartureAt(), city).discount();
                var passengerDiscount = configService.get(userRols).valu();
                var discountValue = 1  -  passengerDiscount - weatherDiscount;
                price = price.multiply(new BigDecimal(discountValue));
            }
            return price;
        }

        List<RouteStop> routeStops = tripRepository.findRouteStopsByUserId(origin, destination, trip.getId());
         price =routeStops.stream().map(f -> {
            BigDecimal priceTemp =  f.getFareRule().getBasePrice();
            if (f.getFareRule().getIsDynamicPricing()){
                var passengerDiscount = configService.get(userRols).valu();
                var city = trip.getRoute().getOrigin().getCity().getId();
                var weatherDiscount = weatherService.get(trip.getDate(), trip.getDepartureAt(), city).discount();
                var discountValue = 1 - passengerDiscount - weatherDiscount  ;
                priceTemp = priceTemp.multiply(new BigDecimal(discountValue));
            }
            return priceTemp;
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
        return price;
    }






}
