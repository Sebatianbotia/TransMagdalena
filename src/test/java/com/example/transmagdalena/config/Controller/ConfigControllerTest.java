package com.example.transmagdalena.config.Controller;

import com.example.transmagdalena.config.ConfigType;
import com.example.transmagdalena.config.DTO.ConfigDTO;
import com.example.transmagdalena.config.Service.ConfigService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConfigController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConfigControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private ConfigService configService;

    @MockitoBean
    private com.example.transmagdalena.Security.Config.jwt.JwtService jwtService;

    private ConfigDTO.configResponse config(Long id) {
        return new ConfigDTO.configResponse(
                id,
                ConfigType.PASSENGER_DISCOUNT,
                0.25F
        );
    }

    @Test
    void get_shouldReturn200() throws Exception {

        when(configService.get(3L)).thenReturn(config(3L));

        mvc.perform(get("/api/v1/config/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.type").value("PASSENGER_DISCOUNT"))
                .andExpect(jsonPath("$.value").value(0.25F));

        verify(configService).get(3L);
    }

    @Test
    void getAll_shouldReturn200() throws Exception {

        var p = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(List.of(config(1L), config(2L)));

        when(configService.getAll(p)).thenReturn(page);

        mvc.perform(get("/api/v1/config/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(configService).getAll(p);
    }

    @Test
    void create_shouldReturn201AndLocation() throws Exception {

        var req = new ConfigDTO.configCreateRequest(
                ConfigType.PASSENGER_DISCOUNT,
                0.20F
        );

        var saved = new ConfigDTO.configResponse(
                10L,
                ConfigType.PASSENGER_DISCOUNT,
                0.20F
        );

        when(configService.save(any())).thenReturn(saved);

        mvc.perform(post("/api/v1/config/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/api/v1/config/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.value").value(0.20F));

        verify(configService).save(any());
    }

    @Test
    void update_shouldReturn200() throws Exception {

        var req = new ConfigDTO.configUpdateRequest(
                ConfigType.PASSENGER_DISCOUNT,
                0.30F
        );

        var updated = new ConfigDTO.configResponse(
                15L,
                ConfigType.PASSENGER_DISCOUNT,
                0.30F
        );

        when(configService.update(eq(15L), any())).thenReturn(updated);

        mvc.perform(patch("/api/v1/config/update/15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.type").value("PASSENGER_DISCOUNT"))
                .andExpect(jsonPath("$.value").value(0.30F));

        verify(configService).update(eq(15L), any());
    }

    @Test
    void delete_shouldReturn204() throws Exception {

        doNothing().when(configService).delete(7L);

        mvc.perform(delete("/api/v1/config/delete/7"))
                .andExpect(status().isNoContent());

        verify(configService).delete(7L);
    }
}
