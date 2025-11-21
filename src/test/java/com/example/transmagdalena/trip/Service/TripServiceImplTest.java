package com.example.transmagdalena.trip.Service;

import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.bus.service.BusService;
import com.example.transmagdalena.config.ConfigType;
import com.example.transmagdalena.config.DTO.ConfigDTO;
import com.example.transmagdalena.config.Service.ConfigService;
import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.fareRule.Service.FareRuleService;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.route.service.RouteService;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.Mapper.TripMapper;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.trip.TripStatus;
import com.example.transmagdalena.trip.repository.TripRepository;
import com.example.transmagdalena.trip.service.TripServiceImpl;
import com.example.transmagdalena.user.UserRols;
import com.example.transmagdalena.weather.Service.WeatherService;
import com.example.transmagdalena.weather.DTO.WeatherDTO;

import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.city.City;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TripServiceImplTest {

    @Mock TripRepository tripRepository;
    @Mock TripMapper tripMapper;
    @Mock BusService busService;
    @Mock FareRuleService fareRuleService;
    @Mock RouteService routeService;
    @Mock ConfigService configService;
    @Mock WeatherService weatherService;

    @InjectMocks
    TripServiceImpl service;

    private Trip trip() {

        Stop origin = Stop.builder()
                .id(1L)
                .city(City.builder().id(5L).build())
                .originTickets(new HashSet<>())
                .destinationTickets(new HashSet<>())
                .originRoutes(new HashSet<>())
                .destinationRoutes(new HashSet<>())
                .build();

        Stop destination = Stop.builder()
                .id(2L)
                .originTickets(new HashSet<>())
                .destinationTickets(new HashSet<>())
                .originRoutes(new HashSet<>())
                .destinationRoutes(new HashSet<>())
                .build();

        Route route = Route.builder()
                .origin(origin)
                .destination(destination)
                .trips(new ArrayList<>())
                .routeStops(new ArrayList<>())
                .build();

        Bus bus = Bus.builder()
                .capacity(40)
                .seats(new HashSet<>())
                .trips(new ArrayList<>())
                .build();

        FareRule fareRule = FareRule.builder()
                .id(50L)
                .basePrice(BigDecimal.valueOf(20000))
                .isDynamicPricing(false)
                .trips(new HashSet<>())
                .build();

        return Trip.builder()
                .id(10L)
                .bus(bus)
                .route(route)
                .fareRule(fareRule)
                .date(LocalDate.now().plusDays(1))
                .departureAt(LocalTime.now().plusHours(1))
                .arrivalAt(LocalTime.now().plusHours(3))
                .tripStatus(TripStatus.SCHEDULED)
                .tickets(new HashSet<>())
                .seatHolds(new HashSet<>())
                .assignments(new HashSet<>())
                .tripQRs(new HashSet<>())
                .build();
    }

    // ============= TESTS =============

    @Test
    void shouldCreateTrip() {

        var req = new TripDTO.tripCreateRequest(
                1L, 2L,
                LocalDate.now().plusDays(1),
                LocalTime.now().plusMinutes(10),
                LocalTime.now().plusHours(2),
                TripStatus.SCHEDULED, 50L
        );

        Bus bus = Bus.builder()
                .trips(new ArrayList<>())
                .seats(new HashSet<>())
                .build();

        Route route = Route.builder()
                .trips(new ArrayList<>())
                .routeStops(new ArrayList<>())
                .build();

        FareRule fare = FareRule.builder()
                .trips(new HashSet<>())
                .build();

        var entity = trip();

        when(tripMapper.toEntity(req)).thenReturn(entity);
        when(busService.getObject(1L)).thenReturn(bus);
        when(routeService.getObject(2L)).thenReturn(route);
        when(fareRuleService.getObject(50L)).thenReturn(fare);
        when(tripRepository.save(any())).thenReturn(entity);
        when(tripMapper.tripResponse(entity))
                .thenReturn(new TripDTO.tripResponse(
                        10L, "", "", null, null, null,
                        BigDecimal.ZERO, TripStatus.SCHEDULED, ""
                ));

        var res = service.save(req);

        assertThat(res.id()).isEqualTo(10L);
    }

    @Test
    void shouldUpdateTrip() {

        var t = trip();

        var req = new TripDTO.tripUpdateRequest(
                99L, 88L, null, null, null,
                TripStatus.DEPARTED, 55L
        );

        doAnswer(inv -> {
            TripDTO.tripUpdateRequest r = inv.getArgument(0);
            Trip tr = inv.getArgument(1);
            if (r.tripStatus() != null) tr.setTripStatus(r.tripStatus());
            return null;
        }).when(tripMapper).update(any(), any());

        when(tripRepository.findById(10L)).thenReturn(Optional.of(t));
        when(busService.getObject(99L)).thenReturn(Bus.builder()
                .trips(new ArrayList<>())
                .seats(new HashSet<>())
                .build());
        when(routeService.getObject(88L)).thenReturn(Route.builder()
                .trips(new ArrayList<>())
                .routeStops(new ArrayList<>())
                .build());
        when(fareRuleService.getObject(55L)).thenReturn(FareRule.builder()
                .trips(new HashSet<>())
                .build());

        when(tripRepository.save(any())).thenReturn(t);
        when(tripMapper.tripResponse(t))
                .thenReturn(new TripDTO.tripResponse(
                        10L,"","",null,null,null,
                        BigDecimal.ZERO,TripStatus.DEPARTED,""
                ));

        var res = service.update(req, 10L);

        assertThat(res.status()).isEqualTo(TripStatus.DEPARTED);
    }

    @Test
    void shouldDeleteTripSetsStatusCancelled() {

        var t = trip();

        // Simula mapper.update()
        doAnswer(inv -> {
            TripDTO.tripUpdateRequest r = inv.getArgument(0);
            Trip tr = inv.getArgument(1);
            tr.setTripStatus(r.tripStatus());
            return null;
        }).when(tripMapper).update(any(), any());

        when(tripRepository.findById(10L)).thenReturn(Optional.of(t));
        when(tripRepository.save(any())).thenReturn(t);

        when(tripMapper.tripResponse(any())).thenReturn(
                new TripDTO.tripResponse(
                        10L,"","",null,null,null,
                        BigDecimal.ZERO,TripStatus.CANCELLED,""
                )
        );

        service.delete(10L);

        assertThat(t.getTripStatus()).isEqualTo(TripStatus.CANCELLED);
    }


    @Test
    void shouldFindTripsBetweenStops() {

        var t = trip();
        Pageable pageable = PageRequest.of(0,10);

        when(tripRepository.findAllTripsBetweenOriginAndDestination(
                eq(1L), eq(2L), eq(t.getDate()), eq(pageable)
        )).thenReturn(new PageImpl<>(List.of(t)));

        when(tripRepository.findBusySeats(t.getId(),1L)).thenReturn(List.of());

        lenient().when(configService.get(any(UserRols.class)))
                .thenReturn(new ConfigDTO.configResponse(
                        2L,
                        ConfigType.PASSENGER_DISCOUNT,
                        0.3F
                ));

        lenient().when(weatherService.get(any(), any(), anyLong()))
                .thenReturn(new WeatherDTO.weatherRespose(
                        1L, null, 0F, null, null, null, 5L
                ));

        when(tripMapper.tripResponseWithAvailableSeat(any(), anyInt(), any()))
                .thenReturn(new TripDTO.tripResponseWithSeatAvailable(
                        new TripDTO.tripResponse(
                                10L,"","",null,null,null,
                                BigDecimal.ZERO,TripStatus.SCHEDULED,""
                        ),
                        40
                ));

        var result = service.findTripsBetweenStops(
                1L,2L,pageable, UserRols.PASSENGER, t.getDate()
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).seatAvailable()).isEqualTo(40);
    }


    @Test
    void shouldCalculatePriceDynamic() {

        var t = trip();
        t.getFareRule().setIsDynamicPricing(true);

        when(configService.get(any(UserRols.class)))
                .thenReturn(new ConfigDTO.configResponse(
                        1L,
                        ConfigType.PASSENGER_DISCOUNT,
                        0.10F
                ));

        when(weatherService.get(any(), any(), eq(5L)))
                .thenReturn(new WeatherDTO.weatherRespose(
                        1L, null, 0.10F, null, null, null, 5L
                ));

        BigDecimal price = service.calculatePrice(
                t,1L,2L,UserRols.PASSENGER
        );

        assertThat(price).isEqualTo(BigDecimal.valueOf(16000.00)); // funciona, hay una minima diferencia
    }

}
