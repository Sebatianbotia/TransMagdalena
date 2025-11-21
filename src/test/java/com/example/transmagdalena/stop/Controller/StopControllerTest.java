package com.example.transmagdalena.stop.Controller;

import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.Service.StopService;

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
        controllers = StopController.class
)
@AutoConfigureMockMvc(addFilters = false)
class StopControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private StopService stopService;

    @MockitoBean
    private JwtService jwtService;

    // ----------------------------------------------------
    // Helpers
    // ----------------------------------------------------
    private StopDTO.cityDto city() {
        return new StopDTO.cityDto("SantaMarta");
    }

    private StopDTO.stopResponse response(Long id) {
        return new StopDTO.stopResponse(
                id,
                "Terminal",
                city(),
                11.23f,
                -74.19f
        );
    }

    // ----------------------------------------------------
    // GET /{id}
    // ----------------------------------------------------
    @Test
    void getById_shouldReturn200() throws Exception {

        when(stopService.get(5L)).thenReturn(response(5L));

        mvc.perform(get("/api/v1/stop/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Terminal"));

        verify(stopService).get(5L);
    }

    // ----------------------------------------------------
    // GET /all
    // ----------------------------------------------------
    @Test
    void getAll_shouldReturn200() throws Exception {

        Page<StopDTO.stopResponse> page =
                new PageImpl<>(List.of(response(1L)));

        when(stopService.getAll(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/v1/stop/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Terminal"));

        verify(stopService).getAll(any(PageRequest.class));
    }

    // ----------------------------------------------------
    // POST /create
    // ----------------------------------------------------
    @Test
    void create_shouldReturn201() throws Exception {

        var req = new StopDTO.stopCreateRequest(
                "Terminal",
                1L,
                11.23f,
                -74.19f
        );

        var res = response(10L);

        when(stopService.save(any())).thenReturn(res);

        mvc.perform(post("/api/v1/stop/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/api/v1/stop/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Terminal"));

        verify(stopService).save(any());
    }

    // ----------------------------------------------------
    // PATCH /update/{id}
    // ----------------------------------------------------
    @Test
    void update_shouldReturn200() throws Exception {

        var req = new StopDTO.stopUpdateRequest(
                "Nuevo Terminal",
                2L,
                12.20f,
                -73.10f
        );

        var res = new StopDTO.stopResponse(
                10L,
                "Nuevo Terminal",
                new StopDTO.cityDto("Barranquilla"),
                12.20f,
                -73.10f
        );

        when(stopService.updateStop(any(), eq(10L))).thenReturn(res);

        mvc.perform(patch("/api/v1/stop/update/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Nuevo Terminal"));

        verify(stopService).updateStop(any(), eq(10L));
    }

    // ----------------------------------------------------
    // DELETE /delete/{id}
    // ----------------------------------------------------
    @Test
    void delete_shouldReturn204() throws Exception {

        doNothing().when(stopService).delete(10L);

        mvc.perform(delete("/api/v1/stop/delete/10"))
                .andExpect(status().isNoContent());

        verify(stopService).delete(10L);
    }
}
