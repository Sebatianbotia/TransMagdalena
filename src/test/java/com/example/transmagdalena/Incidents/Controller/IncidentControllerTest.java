package com.example.transmagdalena.incidents.Controller;

import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.example.transmagdalena.incidents.DTO.IncidentDTO;
import com.example.transmagdalena.incidents.EntityType;
import com.example.transmagdalena.incidents.IncidentType;
import com.example.transmagdalena.incidents.service.IncidentServiceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = IncidentController.class)
@AutoConfigureMockMvc(addFilters = false)
class IncidentControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    IncidentServiceImpl incidentService;

    @MockitoBean
    private JwtService jwtService;

    private IncidentDTO.incidentResponse sample(Long id) {
        return new IncidentDTO.incidentResponse(
                id,
                IncidentType.DELIVERY_FAIL,
                99L,
                EntityType.TRIP,
                "test note",
                LocalDateTime.of(2025, 1, 1, 10, 0)
        );
    }

    @Test
    void create_shouldReturn201() throws Exception {

        var req = new IncidentDTO.incidentCreateRequest(
                IncidentType.DELIVERY_FAIL,
                99L,
                EntityType.TRIP,
                "test note",
                LocalDateTime.of(2025,1,1,10,0)
        );

        when(incidentService.save(any()))
                .thenReturn(sample(10L));

        mvc.perform(post("/api/v1/incident/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.incidentType").value("DELIVERY_FAIL"));
    }

    @Test
    void update_shouldReturn200() throws Exception {

        var req = new IncidentDTO.incidentUpdateRequest(
                IncidentType.DELIVERY_FAIL,
                33L,
                EntityType.TICKET,
                "updated note",
                LocalDateTime.of(2026,2,2,12,0)
        );

        var updated = new IncidentDTO.incidentResponse(
                10L,
                IncidentType.DELIVERY_FAIL,
                33L,
                EntityType.TICKET,
                "updated note",
                LocalDateTime.of(2026,2,2,12,0)
        );

        when(incidentService.update(any(), eq(10L)))
                .thenReturn(updated);

        mvc.perform(patch("/api/v1/incident/update/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incidentType").value("DELIVERY_FAIL"))
                .andExpect(jsonPath("$.entityType").value("TICKET"));
    }

    @Test
    void getAll_shouldReturnPage() throws Exception {

        Page<?> page = new PageImpl<>(List.of(sample(5L)));

        when(incidentService.getAll(any()))
                .thenReturn((Page) page);

        mvc.perform(get("/api/v1/incident/all")
                        .param("page","0")
                        .param("size","5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(5))
                .andExpect(jsonPath("$.content[0].entityType").value("TRIP"));
    }

    @Test
    void getIncidentByEntity_shouldReturn200() throws Exception {

        Page<?> page = new PageImpl<>(List.of(
                new IncidentDTO.incidentResponse(
                        7L,
                        IncidentType.DELIVERY_FAIL,
                        99L,
                        EntityType.TICKET,
                        "note",
                        LocalDateTime.of(2025,1,1,10,0)
                )
        ));

        when(incidentService.getIncidentByIdAndEntityType(99L, EntityType.TICKET, PageRequest.of(0,5)))
                .thenReturn((Page) page);

        mvc.perform(get("/api/v1/incident/find")
                        .param("entityId","99")
                        .param("entityType","TICKET")
                        .param("page","0")
                        .param("size","5"))
                .andExpect(status().isOk());
    }
}
