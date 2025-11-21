package com.example.transmagdalena.routeStop.Controller;

import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.service.RouteStopService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;

import org.springframework.data.domain.*;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = RouteStopController.class
)
@AutoConfigureMockMvc(addFilters = false)
class RouteStopControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private RouteStopService routeStopService;

    @MockitoBean
    private JwtService jwtService;

    // ---------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------

    private RouteStopDTO.stopDTO stop(Long id) {
        return new RouteStopDTO.stopDTO(id, "Stop" + id, 10.0f, 20.0f);
    }

    private RouteStopDTO.routeDTO route(Long id) {
        return new RouteStopDTO.routeDTO(id, "R00" + id, stop(1L), stop(2L));
    }

    private RouteStopDTO.fareRuleDTO fare(Long id) {
        return new RouteStopDTO.fareRuleDTO(id, BigDecimal.valueOf(10000), false);
    }

    private RouteStopDTO.routeStopResponse response(Long id) {
        return new RouteStopDTO.routeStopResponse(
                id,
                1,
                route(1L),
                stop(1L),
                stop(2L),
                fare(5L)
        );
    }

    // ---------------------------------------------------------
    // GET /{id}
    // ---------------------------------------------------------

    @Test
    void getById_shouldReturn200() throws Exception {

        when(routeStopService.get(10L)).thenReturn(response(10L));

        mvc.perform(get("/api/v1/routeStop/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.stopOrder").value(1));

        verify(routeStopService).get(10L);
    }

    // ---------------------------------------------------------
    // GET /all
    // ---------------------------------------------------------

    @Test
    void getAll_shouldReturn200() throws Exception {

        Page<RouteStopDTO.routeStopResponse> page =
                new PageImpl<>(List.of(response(1L)));

        when(routeStopService.getAll(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/v1/routeStop/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(routeStopService).getAll(any(PageRequest.class));
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------

    @Test
    void create_shouldReturn201() throws Exception {

        var req = new RouteStopDTO.routeStopCreateRequest(
                1,
                1L,
                2L,
                3L,
                new RouteStopDTO.fareRuleCreateRequest(
                        BigDecimal.valueOf(10000),
                        false
                )
        );

        var res = response(10L);

        when(routeStopService.save(any())).thenReturn(res);

        mvc.perform(post("/api/v1/routeStop/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/api/v1/stop/10")))
                .andExpect(jsonPath("$.id").value(10));

        verify(routeStopService).save(any());
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Test
    void update_shouldReturn200() throws Exception {

        var req = new RouteStopDTO.routeStopUpdateRequest(
                2,
                4L,
                5L,
                6L,
                new RouteStopDTO.fareRuleUpdateRequest(
                        6L,
                        BigDecimal.valueOf(20000),
                        true
                )
        );

        var res = new RouteStopDTO.routeStopResponse(
                10L, 2, route(6L), stop(4L), stop(5L),
                new RouteStopDTO.fareRuleDTO(8L, BigDecimal.valueOf(20000), true)
        );

        when(routeStopService.update(eq(10L), any())).thenReturn(res);

        mvc.perform(patch("/api/v1/routeStop/update/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.stopOrder").value(2));

        verify(routeStopService).update(eq(10L), any());
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------

    @Test
    void delete_shouldReturn204() throws Exception {

        doNothing().when(routeStopService).delete(10L);

        mvc.perform(delete("/api/v1/routeStop/delete/10"))
                .andExpect(status().isNoContent());

        verify(routeStopService).delete(10L);
    }
}
