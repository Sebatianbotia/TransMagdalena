package com.example.transmagdalena.fareRule.Controller;

import com.example.transmagdalena.fareRule.DTO.FareRuleDTO;
import com.example.transmagdalena.fareRule.Service.FareRuleService;
import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FareRuleController.class)
@AutoConfigureMockMvc(addFilters = false)
class FareRuleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private FareRuleService fareRuleService;

    @MockitoBean
    private JwtService jwtService;


    private FareRuleDTO.stopDTO stop(Long id, String name) {
        return new FareRuleDTO.stopDTO(id, name, 10.0F, 20.0F);
    }

    private FareRuleDTO.fareRuleResponse response(Long id) {
        return new FareRuleDTO.fareRuleResponse(
                id,
                stop(1L, "Origin"),
                stop(2L, "Destination"),
                BigDecimal.valueOf(15000),
                true
        );
    }


    @Test
    void get_shouldReturn200() throws Exception {

        when(fareRuleService.get(3L)).thenReturn(response(3L));

        mvc.perform(get("/api/v1/fareRule/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.origin.name").value("Origin"))
                .andExpect(jsonPath("$.destination.name").value("Destination"));

        verify(fareRuleService).get(3L);
    }


    @Test
    void getAll_shouldReturn200() throws Exception {

        var pageReq = PageRequest.of(1, 5, Sort.by("id").ascending());
        var list = List.of(response(1L), response(2L));

        when(fareRuleService.getAll(pageReq))
                .thenReturn(new PageImpl<>(list, pageReq, list.size()));

        mvc.perform(get("/api/v1/fareRule/all?page=1&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(fareRuleService).getAll(pageReq);
    }


    @Test
    void create_shouldReturn201AndLocation() throws Exception {

        var req = new FareRuleDTO.fareRuleCreateRequest(
                1L, 2L,
                BigDecimal.valueOf(20000),
                true
        );

        var saved = response(10L);

        when(fareRuleService.save(any())).thenReturn(saved);

        mvc.perform(post("/api/v1/fareRule/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/api/v1/fareRule/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.isDynamicPricing").value(true));

        verify(fareRuleService).save(any());
    }


    @Test
    void update_shouldReturn200() throws Exception {

        var req = new FareRuleDTO.fareRuleUpdateRequest(
                1L, 2L,
                BigDecimal.valueOf(18000),
                false
        );

        var updated = new FareRuleDTO.fareRuleResponse(
                20L,
                stop(1L, "Origin"),
                stop(2L, "Destination"),
                BigDecimal.valueOf(18000),
                false
        );

        when(fareRuleService.update(any(), eq(20L))).thenReturn(updated);
        mvc.perform(patch("/api/v1/fareRule/update/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.basePrice").value(18000))
                .andExpect(jsonPath("$.isDynamicPricing").value(false));

        verify(fareRuleService).update(any(), eq(20L));    }


    @Test
    void delete_shouldReturn204() throws Exception {

        doNothing().when(fareRuleService).delete(5L);

        mvc.perform(delete("/api/v1/fareRule/delete/5"))
                .andExpect(status().isNoContent());

        verify(fareRuleService).delete(5L);
    }

}
