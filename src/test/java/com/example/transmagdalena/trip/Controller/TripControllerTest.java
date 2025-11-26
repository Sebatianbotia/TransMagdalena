package com.example.transmagdalena.trip.Controller;

import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.example.transmagdalena.incidents.DTO.IncidentDTO;
import com.example.transmagdalena.incidents.EntityType;
import com.example.transmagdalena.incidents.service.IncidentService;
import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.trip.DTO.TripDTO;
import com.example.transmagdalena.trip.TripStatus;
import com.example.transmagdalena.trip.controller.TripController;
import com.example.transmagdalena.trip.service.TripService;
import com.example.transmagdalena.user.UserRols;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.*;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TripController.class)
@AutoConfigureMockMvc(addFilters = false)
class TripControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private TripService tripService;

    @MockitoBean
    private IncidentService incidentService;

    @MockitoBean
    private JwtService jwtService;

    // ============================
    // HELPERS
    // ============================

    private TripDTO.tripResponse tripResponse(Long id) {
        return new TripDTO.tripResponse(
                id,
                "Origen",
                "Destino",
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                LocalDate.of(2025, 1, 1),
                null,
                null,
                "BUS123"
        );
    }

    private IncidentDTO.incidentResponse incident(Long id) {
        return new IncidentDTO.incidentResponse(
                id,
                null,
                99L,
                EntityType.TRIP,
                "note",
                null
        );
    }

    // ============================
    // GET /{id}
    // ============================

    @Test
    void getTrip_shouldReturn200() throws Exception {

        var res = tripResponse(10L);

        when(tripService.get(10L)).thenReturn(res);

        mvc.perform(get("/api/v1/trip/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        verify(tripService).get(10L);
    }

    // ============================
    // GET /{id}/incidents
    // ============================

    @Test
    void getIncidents_shouldReturn200() throws Exception {

        Page<IncidentDTO.incidentResponse> page =
                new PageImpl<>(List.of(incident(1L)));

        when(incidentService.getIncidentByIdAndEntityType(eq(10L), eq(EntityType.TRIP), any(Pageable.class)))
                .thenReturn(page);

        mvc.perform(get("/api/v1/trip/10/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(incidentService).getIncidentByIdAndEntityType(eq(10L), eq(EntityType.TRIP), any(Pageable.class));
    }

    // ============================
    // GET /{id}/busySeats
    // ============================

    @Test
    void getBusySeats_shouldReturn200() throws Exception {

        when(tripService.getBusySeats(10L, 1L))
                .thenReturn(List.of(5, 7, 9));

        mvc.perform(get("/api/v1/trip/10/busySeats")
                        .param("origin", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(5));

        verify(tripService).getBusySeats(10L, 1L);
    }

    // ============================
    // GET /{id}/seats
    // ============================

//    @Test
//    void getTripSeats_shouldReturn200() throws Exception {
//
//        Seat seat1 = new Seat();
//        seat1.setId(1L);
//
//        Seat seat2 = new Seat();
//        seat2.setId(2L);
//
//        when(tripService.getTripSeats(10L))
//                .thenReturn(Set.of(seat1, seat2));
//
//        mvc.perform(get("/api/v1/trip/10/seats"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").exists());
//
//        verify(tripService).getTripSeats(10L);
//    }

    // ============================
    // GET /all
    // ============================

    @Test
    void getAll_shouldReturn200() throws Exception {

        Page<TripDTO.tripResponse> page =
                new PageImpl<>(List.of(tripResponse(1L)));

        when(tripService.getAll(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/v1/trip/all?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(tripService).getAll(any(PageRequest.class));
    }

    // ============================
    // PATCH /update/{id}
    // ============================

    @Test
    void update_shouldReturn200() throws Exception {

        var req = new TripDTO.tripUpdateRequest(
                1L, 1L,
                LocalDate.of(2025,1,1),
                LocalTime.of(8,0),
                LocalTime.of(10,0),
                null,
                1L
        );

        var res = tripResponse(10L);

        when(tripService.update(eq(req), eq(10L))).thenReturn(res);

        mvc.perform(patch("/api/v1/trip/update/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        verify(tripService).update(eq(req), eq(10L));
    }

    // ============================
    // DELETE /cancel/{id}
    // ============================

    @Test
    void cancel_shouldReturn204() throws Exception {

        doNothing().when(tripService).delete(10L);

        mvc.perform(delete("/api/v1/trip/cancel/10"))
                .andExpect(status().isNoContent());

        verify(tripService).delete(10L);
    }

    // ============================
    // POST /create
    // ============================

    @Test
    void create_shouldReturn200() throws Exception {

        var req = new TripDTO.tripCreateRequest(
                1L,
                1L,
                LocalDate.of(2025,1,1),
                LocalTime.of(10,0),
                LocalTime.of(12,0),
                TripStatus.SCHEDULED,   // <-- FIX
                1L
        );

        var res = tripResponse(10L);

        when(tripService.save(any())).thenReturn(res);

        mvc.perform(post("/api/v1/trip/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        verify(tripService).save(any());
    }

}
