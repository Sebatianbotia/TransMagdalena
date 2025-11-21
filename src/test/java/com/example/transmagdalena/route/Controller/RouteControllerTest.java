package com.example.transmagdalena.route.Controller;

import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.example.transmagdalena.route.DTO.RouteDTO;
import com.example.transmagdalena.route.controller.RouteController;
import com.example.transmagdalena.route.service.RouteServiceImpl;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
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

@WebMvcTest(RouteController.class)
@AutoConfigureMockMvc(addFilters = false)
class RouteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private RouteServiceImpl routeService;

    @MockitoBean
    private JwtService jwtService;

    // Helpers ---------------------------------------------------------

    private RouteDTO.routeResponse response(Long id) {
        return new RouteDTO.routeResponse(
                id,
                "R001",
                "ORIGIN",
                "DESTINATION",
                "100",
                "2h"
        );
    }

    private RouteDTO.routeResponseStops responseStops() {
        return new RouteDTO.routeResponseStops(
                response(1L),
                List.of(
                        new RouteDTO.routeStopDTO(
                                10L, 1, "ORIGIN", "STOP1", null, false
                        ),
                        new RouteDTO.routeStopDTO(
                                11L, 2, "STOP1", "DESTINATION", null, false
                        )
                )
        );
    }

    // GET /{id} -------------------------------------------------------

    @Test
    void get_shouldReturn200() throws Exception {
        var res = response(5L);

        when(routeService.get(5L)).thenReturn(res);

        mvc.perform(get("/api/v1/route/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.code").value("R001"))
                .andExpect(jsonPath("$.origin").value("ORIGIN"));

        verify(routeService).get(5L);
    }

    // GET /{id}/stops -------------------------------------------------

    @Test
    void getRouteStops_shouldReturn200() throws Exception {

        var res = responseStops();

        when(routeService.getRouteStops(1L)).thenReturn(res);

        mvc.perform(get("/api/v1/route/1/stops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route.id").value(1))
                .andExpect(jsonPath("$.routeStops[0].id").value(10))
                .andExpect(jsonPath("$.routeStops[1].id").value(11));

        verify(routeService).getRouteStops(1L);
    }

    // GET /all --------------------------------------------------------

    @Test
    void getAll_shouldReturn200() throws Exception {

        Page<RouteDTO.routeResponse> page =
                new PageImpl<>(List.of(response(1L)));

        when(routeService.getAll(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/v1/route/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].code").value("R001"));

        verify(routeService).getAll(any(PageRequest.class));
    }

    // GET /count ------------------------------------------------------

    @Test
    void count_shouldReturn200() throws Exception {

        when(routeService.count()).thenReturn(15L);

        mvc.perform(get("/api/v1/route/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("15"));

        verify(routeService).count();
    }

    // CREATE ----------------------------------------------------------

    @Test
    void create_shouldReturn201AndLocation() throws Exception {

        var req = new RouteDTO.routeCreateRequest(
                "R001",
                1L,
                2L
        );

        var res = response(10L);

        when(routeService.save(any())).thenReturn(res);

        mvc.perform(post("/api/v1/route/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/api/v1/route/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.code").value("R001"));

        verify(routeService).save(any());
    }

    // UPDATE ----------------------------------------------------------

    @Test
    void update_shouldReturn200() throws Exception {

        var req = new RouteDTO.routeUpdateRequest(
                "R002",
                5L,
                6L
        );

        var res = new RouteDTO.routeResponse(
                10L,
                "R002",
                "ORIGIN2",
                "DEST2",
                "120",
                "3h"
        );

        when(routeService.update(eq(10L), any())).thenReturn(res);

        mvc.perform(patch("/api/v1/route/update/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.code").value("R002"));

        verify(routeService).update(eq(10L), any());
    }
}
