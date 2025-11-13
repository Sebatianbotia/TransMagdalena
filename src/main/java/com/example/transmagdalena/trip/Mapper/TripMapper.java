package com.example.transmagdalena.trip.Mapper;

import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Trip;
import org.mapstruct.*;

import java.math.BigDecimal;


@Mapper(componentModel = "spring")
public interface TripMapper {


    @Mapping(target = "id", ignore= true)
    @Mapping( target = "bus", ignore = true)
    @Mapping( target = "route", ignore = true)
    @Mapping( target = "fareRule", ignore = true)
    Trip toEntity(TripDTO.tripCreateRequest tripDTO);

    @Mapping( target = "bus", ignore = true)
    @Mapping( target = "route", ignore = true)
    @Mapping( target = "fareRule", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(TripDTO.tripUpdateRequest tripUpdateRequest, @MappingTarget Trip trip);

    default TripDTO.tripResponse tripResponse(Trip trip){
        return new TripDTO.tripResponse(
                trip.getId(), trip.getRoute().getOrigin().getName(),
                trip.getRoute().getDestination().getName(),
                trip.getDepartureAt(), trip.getArrivalAt(),
                trip.getDate(),
                trip.getFareRule().getBasePrice(), trip.getTripStatus(),
                trip.getBus().getPlate()
        );
    }

    default TripDTO.tripResponse tripResponse(Trip trip, BigDecimal price){
        return new TripDTO.tripResponse(
                trip.getId(), trip.getRoute().getOrigin().getName(),
                trip.getRoute().getDestination().getName(),
                trip.getDepartureAt(), trip.getArrivalAt(),
                trip.getDate(),
                price, trip.getTripStatus(),
                trip.getBus().getPlate()
        );
    }

    default TripDTO.tripResponseWithSeatAvailable tripResponseWithAvailableSeat(Trip trip, Integer seatAvailable){
        return new TripDTO.tripResponseWithSeatAvailable(tripResponse(trip), seatAvailable);
    }

    default TripDTO.tripResponseWithSeatAvailable tripResponseWithAvailableSeat(Trip trip, Integer seatAvailable, BigDecimal price){
        return new TripDTO.tripResponseWithSeatAvailable(tripResponse(trip, price), seatAvailable);
    }

}
