package com.example.transmagdalena.ticket.Controller;

import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.example.transmagdalena.seat.SeatType;
import com.example.transmagdalena.seatHold.SeatHoldStatus;
import com.example.transmagdalena.ticket.DTO.TicketDTO;
import com.example.transmagdalena.ticket.TicketPaymentMethod;
import com.example.transmagdalena.ticket.TicketStatus;
import com.example.transmagdalena.ticket.service.TicketService;

import com.example.transmagdalena.user.UserRols;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TicketController.class
)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private TicketService ticketService;

    @MockitoBean
    private JwtService jwtService;

    // ------------------------------------------------------
    // Helpers
    // ------------------------------------------------------

    private TicketDTO.stopDTO stop(Long id, String name) {
        return new TicketDTO.stopDTO(id, name, "SantaMarta");
    }

    private TicketDTO.seatHoldDTO seatHold() {
        return new TicketDTO.seatHoldDTO(
                99L,
                SeatHoldStatus.HOLD,
                12,
                SeatType.PREFERENTIAL
        );
    }

    private TicketDTO.userDTO user() {
        return new TicketDTO.userDTO(
                "Carlos",
                "carlos@example.com",
                "3001234567",
                UserRols.PASSENGER
        );
    }

    private TicketDTO.ticketResponse response(Long id) {
        return new TicketDTO.ticketResponse(
                id,
                seatHold(),
                user(),
                stop(1L, "Origen"),
                stop(2L, "Destino"),
                BigDecimal.valueOf(50000),
                TicketStatus.SOLD,
                TicketPaymentMethod.CASH,
                "qr-url"
        );
    }

    // ------------------------------------------------------
    // CREATE
    // ------------------------------------------------------

    @Test
    void create_shouldReturn200() throws Exception {

        var req = new TicketDTO.ticketCreateRequest(
                1L,
                50L,
                10L,
                1L,
                2L,
                BigDecimal.valueOf(50000),
                TicketStatus.SOLD,
                TicketPaymentMethod.CASH
        );

        var res = response(100L);

        when(ticketService.create(any())).thenReturn(res);

        mvc.perform(post("/api/v1/ticket/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.price").value(50000))
                .andExpect(jsonPath("$.status").value("SOLD"));

        verify(ticketService).create(any());
    }

    // ------------------------------------------------------
    // GET BY ID
    // ------------------------------------------------------

    @Test
    void getById_shouldReturn200() throws Exception {

        when(ticketService.get(5L)).thenReturn(response(5L));

        mvc.perform(get("/api/v1/ticket/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.origin.name").value("Origen"));

        verify(ticketService).get(5L);
    }

    // ------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------

    @Test
    void update_shouldReturn200() throws Exception {

        var req = new TicketDTO.ticketUpdateRequest(
                2L,
                51L,
                11L,
                3L,
                4L,
                BigDecimal.valueOf(60000),
                TicketStatus.SOLD,
                TicketPaymentMethod.CARD
        );

        var res = new TicketDTO.ticketResponse(
                10L,
                seatHold(),
                user(),
                stop(3L, "NewOrigin"),
                stop(4L, "NewDest"),
                BigDecimal.valueOf(60000),
                TicketStatus.SOLD,
                TicketPaymentMethod.CARD,
                "new-qr"
        );

        when(ticketService.update(eq(10L), any())).thenReturn(res);

        mvc.perform(patch("/api/v1/ticket/update/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("SOLD"))
                .andExpect(jsonPath("$.paymentMethod").value("CARD"));

        verify(ticketService).update(eq(10L), any());
    }

    // ------------------------------------------------------
    // DELETE / CANCEL
    // ------------------------------------------------------

    @Test
    void cancel_shouldReturn200() throws Exception {

        doNothing().when(ticketService).delete(10L);

        mvc.perform(delete("/api/v1/ticket/cancel/10"))
                .andExpect(status().isOk());

        verify(ticketService).delete(10L);
    }
}
