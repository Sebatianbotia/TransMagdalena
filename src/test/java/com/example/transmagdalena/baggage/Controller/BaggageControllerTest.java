package com.example.transmagdalena.baggage.Controller;

import com.example.transmagdalena.baggage.DTO.BaggageDTO;
import com.example.transmagdalena.baggage.controller.BaggageController;
import com.example.transmagdalena.baggage.service.BaggageService;
import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.*;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BaggageController.class)
@AutoConfigureMockMvc(addFilters = false)
class BaggageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private BaggageService baggageService;

    @MockitoBean
    private JwtService jwtService;

    // Helpers ------------------------------------------------

    private BaggageDTO.baggageResponse response(Long id) {
        return new BaggageDTO.baggageResponse(id, 20, "TAG123", BigDecimal.valueOf(5000));
    }

    // CREATE -------------------------------------------------

    @Test
    void create_shouldReturn201AndLocation() throws Exception {

        var req = new BaggageDTO.baggageCreateRequest(
                20,
                "TAG123",
                BigDecimal.valueOf(5000)
        );

        var res = response(10L);

        when(baggageService.save(any())).thenReturn(res);

        mvc.perform(post("/api/v1/baggage/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/baggage/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.tagCode").value("TAG123"))
                .andExpect(jsonPath("$.fee").value(5000));

        verify(baggageService).save(any());
    }

    // GET BY ID ---------------------------------------------

    @Test
    void getById_shouldReturn200() throws Exception {
        var res = response(5L);
        when(baggageService.get("TAG123")).thenReturn(res);

        mvc.perform(get("/api/v1/baggage/TAG123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.weight").value(20))
                .andExpect(jsonPath("$.fee").value(5000));

        verify(baggageService).get("TAG123");
    }

    // GET ALL ------------------------------------------------

    @Test
    void getAll_shouldReturn200() throws Exception {

        var res = response(1L);

        Page<BaggageDTO.baggageResponse> page =
                new PageImpl<>(List.of(res));

        when(baggageService.getAll(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/v1/baggage/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].tagCode").value("TAG123"));

        verify(baggageService).getAll(any(PageRequest.class));
    }

    // UPDATE -------------------------------------------------

    @Test
    void update_shouldReturn200() throws Exception {

        var req = new BaggageDTO.baggageUpdateRequest(
                30,
                "TAG999",
                BigDecimal.valueOf(7000)
        );

        var res = new BaggageDTO.baggageResponse(
                10L, 30, "TAG999", BigDecimal.valueOf(7000)
        );

        when(baggageService.update(any(), eq(10L))).thenReturn(res);

        mvc.perform(patch("/api/v1/baggage/update/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.weight").value(30))
                .andExpect(jsonPath("$.tagCode").value("TAG999"));

        verify(baggageService).update(any(), eq(10L));
    }

    // DELETE -------------------------------------------------

    @Test
    void delete_shouldReturn204() throws Exception {

        doNothing().when(baggageService).delete(10L);

        mvc.perform(delete("/api/v1/baggage/delete/10"))
                .andExpect(status().isNoContent());

        verify(baggageService).delete(10L);
    }
}
