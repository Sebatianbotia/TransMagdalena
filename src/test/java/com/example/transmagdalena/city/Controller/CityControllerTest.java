package com.example.transmagdalena.city.Controller;

import com.example.transmagdalena.city.DTO.CityDTO;
import com.example.transmagdalena.city.service.CityService;
import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.Service.StopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CityController.class)
@AutoConfigureMockMvc(addFilters = false)
class CityControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private CityService cityService;

    @MockitoBean
    private StopService stopService;

    @MockitoBean
    private com.example.transmagdalena.Security.Config.jwt.JwtService jwtService;

    private CityDTO.cityResponse city(Long id) {
        return new CityDTO.cityResponse(
                id,
                "Bogotá",
                4.7F,
                -74.0F,
                Set.of(new CityDTO.stopDTO(1L, "Terminal Norte", 4.75F, -74.05F))
        );
    }

    private StopDTO.stopResponse stop(Long id) {
        return new StopDTO.stopResponse(
                id,
                "Terminal Norte",
                new StopDTO.cityDto("Bogotá"),
                4.75F,
                -74.05F
        );
    }

    @Test
    void get_shouldReturn200() throws Exception {

        when(cityService.get(5L)).thenReturn(city(5L));

        mvc.perform(get("/api/v1/city/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Bogotá"));

        verify(cityService).get(5L);
    }

    @Test
    void getAll_shouldReturn200() throws Exception {

        var pageReq = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<CityDTO.cityResponse> page =
                new PageImpl<>(List.of(city(1L), city(2L)));

        when(cityService.getAll(pageReq)).thenReturn(page);

        mvc.perform(get("/api/v1/city/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(cityService).getAll(pageReq);
    }

    @Test
    void getByName_shouldReturn200() throws Exception {

        when(cityService.get("Bogotá")).thenReturn(city(15L));

        mvc.perform(get("/api/v1/city/name/Bogotá"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.name").value("Bogotá"));

        verify(cityService).get("Bogotá");
    }

    @Test
    void create_shouldReturn201AndLocation() throws Exception {

        CityDTO.cityCreateRequest req =
                new CityDTO.cityCreateRequest("Cali", 3.45F, -76.5F);

        CityDTO.cityResponse created =
                new CityDTO.cityResponse(20L, "Cali", 3.45F, -76.5F, Set.of());

        when(cityService.save(any())).thenReturn(created);

        mvc.perform(post("/api/v1/city/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/api/v1/city/20")))
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.name").value("Cali"));

        verify(cityService).save(any());
    }

    @Test
    void update_shouldReturn200() throws Exception {

        CityDTO.cityUpdateRequest req =
                new CityDTO.cityUpdateRequest("Medellín", 6.25F, -75.6F);

        CityDTO.cityResponse updated =
                new CityDTO.cityResponse(7L, "Medellín", 6.25F, -75.6F, Set.of());

        when(cityService.update(any(), eq(7L))).thenReturn(updated);

        mvc.perform(patch("/api/v1/city/update/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Medellín"))
                .andExpect(jsonPath("$.lat").value(6.25F));

        verify(cityService).update(any(), eq(7L));
    }

    @Test
    void getStops_shouldReturn200() throws Exception {

        when(stopService.getStopsByCity(3L))
                .thenReturn(List.of(stop(1L), stop(2L)));

        mvc.perform(get("/api/v1/city/3/stops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(stopService).getStopsByCity(3L);
    }
}
