package com.example.transmagdalena.seat.Controller;

import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.example.transmagdalena.seat.DTO.SeatDTO;
import com.example.transmagdalena.seat.SeatType;
import com.example.transmagdalena.seat.controller.SeatController;
import com.example.transmagdalena.seat.service.SeatServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.data.domain.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = SeatController.class
)
@AutoConfigureMockMvc(addFilters = false)
class SeatControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private SeatServiceImpl seatService;

    @MockitoBean
    private JwtService jwtService;

    // -------------------------------------------------------
    // Helpers
    // -------------------------------------------------------

    private SeatDTO.busDto bus() {
        return new SeatDTO.busDto("AAA111", com.example.transmagdalena.bus.BusStatus.ACTIVE);
    }

    private SeatDTO.seatResponse response(Long id) {
        return new SeatDTO.seatResponse(
                id,
                5,
                SeatType.PREFERENTIAL,
                bus()
        );
    }

    // -------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------

    @Test
    void getSeat_shouldReturn200() throws Exception {

        when(seatService.get(10L)).thenReturn(response(10L));

        mvc.perform(get("/api/v1/seat/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.number").value(5));

        verify(seatService).get(10L);
    }

    // -------------------------------------------------------
    // GET ALL
    // -------------------------------------------------------

    @Test
    void getAll_shouldReturn200() throws Exception {

        Page<SeatDTO.seatResponse> page =
                new PageImpl<>(List.of(response(1L)));

        when(seatService.getAll(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/v1/seat/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].number").value(5));

        verify(seatService).getAll(any(PageRequest.class));
    }

    // -------------------------------------------------------
    // CREATE
    // -------------------------------------------------------

    @Test
    void create_shouldReturn201() throws Exception {

        var req = new SeatDTO.seatCreateRequest(
                5,
                SeatType.PREFERENTIAL,
                20L
        );

        var res = response(10L);

        when(seatService.save(any())).thenReturn(res);

        mvc.perform(post("/api/v1/seat/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/api/v1/seat/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.type").value("PREFERENTIAL"));

        verify(seatService).save(any());
    }

    // -------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------

    @Test
    void update_shouldReturn200() throws Exception {

        var req = new SeatDTO.seatUpdateRequest(
                7,
                SeatType.PREFERENTIAL,
                30L
        );

        var res = new SeatDTO.seatResponse(
                10L,
                7,
                SeatType.PREFERENTIAL,
                bus()
        );

        when(seatService.update(any(), eq(10L))).thenReturn(res);

        mvc.perform(patch("/api/v1/seat/update/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(7))
                .andExpect(jsonPath("$.type").value("PREFERENTIAL"));

        verify(seatService).update(any(), eq(10L));
    }

    // -------------------------------------------------------
    // DELETE
    // -------------------------------------------------------

    @Test
    void delete_shouldReturn204() throws Exception {

        doNothing().when(seatService).delete(10L);

        mvc.perform(delete("/api/v1/seat/delete/10"))
                .andExpect(status().isNoContent());

        verify(seatService).delete(10L);
    }
}
