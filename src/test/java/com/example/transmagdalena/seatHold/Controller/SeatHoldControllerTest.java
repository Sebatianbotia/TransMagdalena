package com.example.transmagdalena.seatHold.Controller;

import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.example.transmagdalena.seat.SeatType;
import com.example.transmagdalena.seatHold.DTO.SeatHoldDTO;
import com.example.transmagdalena.seatHold.SeatHoldStatus;
import com.example.transmagdalena.seatHold.controller.SeatHoldController;
import com.example.transmagdalena.seatHold.service.SeatHoldService;

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

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = SeatHoldController.class
)
@AutoConfigureMockMvc(addFilters = false)
class SeatHoldControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private SeatHoldService seatHoldService;

    @MockitoBean
    private JwtService jwtService;

    // ----------------------------------------------------
    // Helpers
    // ----------------------------------------------------

    private SeatHoldDTO.userDTO user() {
        return new SeatHoldDTO.userDTO("Juan", "juan@mail.com", "111", UserRols.PASSENGER);
    }

    private SeatHoldDTO.routeDTO route() {
        return new SeatHoldDTO.routeDTO(1L, "R1", "A", "B");
    }

    private SeatHoldDTO.tripDTO trip() {
        return new SeatHoldDTO.tripDTO(20L, route(), LocalTime.parse("10:00"), LocalTime.parse("12:00"));
    }

    private SeatHoldDTO.seatDto seat() {
        return new SeatHoldDTO.seatDto(5, SeatType.PREFERENTIAL);
    }

    private SeatHoldDTO.seatHoldResponse response(Long id) {
        return new SeatHoldDTO.seatHoldResponse(
                id,
                SeatHoldStatus.HOLD,
                user(),
                trip(),
                seat()
        );
    }

    // ----------------------------------------------------
    // GET BY ID
    // ----------------------------------------------------
    @Test
    void get_shouldReturn200() throws Exception {

        when(seatHoldService.get(10L)).thenReturn(response(10L));

        mvc.perform(get("/api/v1/seatHold/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("HOLD"));

        verify(seatHoldService).get(10L);
    }

    // ----------------------------------------------------
    // GET ALL
    // ----------------------------------------------------
    @Test
    void getAll_shouldReturn200() throws Exception {

        Page<SeatHoldDTO.seatHoldResponse> page =
                new PageImpl<>(List.of(response(1L)));

        when(seatHoldService.getAll(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/v1/seatHold/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].status").value("HOLD"));

        verify(seatHoldService).getAll(any(PageRequest.class));
    }

    // ----------------------------------------------------
    // CREATE
    // ----------------------------------------------------
    @Test
    void create_shouldReturn201() throws Exception {

        var req = new SeatHoldDTO.seatHoldCreateRequest(
                1L, 20L, 30L
        );

        var res = response(10L);

        when(seatHoldService.save(any())).thenReturn(res);

        mvc.perform(post("/api/v1/seatHold/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/api/v1/seatHold/10")))
                .andExpect(jsonPath("$.id").value(10));

        verify(seatHoldService).save(any());
    }

    // ----------------------------------------------------
    // UPDATE
    // ----------------------------------------------------
    @Test
    void update_shouldReturn200() throws Exception {

        var req = new SeatHoldDTO.seatHoldUpdateRequest(
                SeatHoldStatus.EXPIRED,
                1L,
                2L,
                3L
        );

        var res = new SeatHoldDTO.seatHoldResponse(
                10L,
                SeatHoldStatus.EXPIRED,
                user(),
                trip(),
                seat()
        );

        when(seatHoldService.update(any(), eq(10L))).thenReturn(res);

        mvc.perform(patch("/api/v1/seatHold/update/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EXPIRED"));

        verify(seatHoldService).update(any(), eq(10L));
    }
}
