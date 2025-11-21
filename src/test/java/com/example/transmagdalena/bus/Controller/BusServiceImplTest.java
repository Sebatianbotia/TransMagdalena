package com.example.transmagdalena.bus.Controller;

import com.example.transmagdalena.bus.BusStatus;
import com.example.transmagdalena.bus.DTO.BusDTO;
import com.example.transmagdalena.bus.service.BusService;
import com.example.transmagdalena.seat.SeatType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusController.class)
@AutoConfigureMockMvc(addFilters = false)
class BusControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    private BusService busService;

    @MockitoBean
    private com.example.transmagdalena.Security.Config.jwt.JwtService jwtService;

    private BusDTO.seatResponseDto seat(Integer n) {
        return new BusDTO.seatResponseDto(n, SeatType.STANDARD);
    }

    private BusDTO.busResponse bus(Long id) {
        return new BusDTO.busResponse(
                id,
                "ABC123",
                40,
                BusStatus.ACTIVE,
                Set.of(seat(1), seat(2))
        );
    }

    @Test
    void getById_shouldReturn200() throws Exception {

        when(busService.get(5L)).thenReturn(bus(5L));

        mvc.perform(get("/api/v1/bus/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.plate").value("ABC123"))
                .andExpect(jsonPath("$.capacity").value(40));

        verify(busService).get(5L);
    }

    @Test
    void create_shouldReturn201AndLocation() throws Exception {

        BusDTO.busCreateRequest req =
                new BusDTO.busCreateRequest(
                        "XYZ987",
                        50,
                        BusStatus.ACTIVE,
                        Set.of(new BusDTO.seatCreateRequest(1, SeatType.STANDARD))
                );

        BusDTO.busResponse created =
                new BusDTO.busResponse(
                        10L,
                        "XYZ987",
                        50,
                        BusStatus.ACTIVE,
                        Set.of(seat(1))
                );

        when(busService.save(any())).thenReturn(created);

        mvc.perform(post("/api/v1/bus/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/api/v1/bus/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.plate").value("XYZ987"))
                .andExpect(jsonPath("$.capacity").value(50));

        verify(busService).save(any());
    }

    @Test
    void getAll_shouldReturn200() throws Exception {

        var pageReq = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<BusDTO.busResponse> page =
                new PageImpl<>(List.of(bus(1L), bus(2L)));

        when(busService.getAll(pageReq)).thenReturn(page);

        mvc.perform(get("/api/v1/bus/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(busService).getAll(pageReq);
    }

    @Test
    void update_shouldReturn200() throws Exception {

        BusDTO.busUpdateRequest req =
                new BusDTO.busUpdateRequest(
                        10L,
                        "UPDATED123",
                        60,
                        BusStatus.MAINTENANCE
                );

        BusDTO.busResponse updated =
                new BusDTO.busResponse(
                        10L,
                        "UPDATED123",
                        60,
                        BusStatus.MAINTENANCE,
                        Set.of(seat(1))
                );

        when(busService.update(any(), eq(10L))).thenReturn(updated);

        mvc.perform(patch("/api/v1/bus/update/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plate").value("UPDATED123"))
                .andExpect(jsonPath("$.capacity").value(60))
                .andExpect(jsonPath("$.status").value("MAINTENANCE"));

        verify(busService).update(any(), eq(10L));
    }

    @Test
    void delete_shouldReturn204() throws Exception {

        mvc.perform(delete("/api/v1/bus/delete/10"))
                .andExpect(status().isNoContent());

        verify(busService).delete(10L);
    }

    @Test
    void count_shouldReturn200() throws Exception {

        when(busService.countBuses()).thenReturn(5L);

        mvc.perform(get("/api/v1/bus/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));

        verify(busService).countBuses();
    }
}
