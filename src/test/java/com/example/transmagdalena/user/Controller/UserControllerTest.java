package com.example.transmagdalena.user.Controller;

import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.example.transmagdalena.ticket.DTO.TicketDTO;
import com.example.transmagdalena.ticket.service.TicketService;
import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.Service.UserService;
import com.example.transmagdalena.user.UserRols;
import com.example.transmagdalena.user.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    UserService userService;

    @MockitoBean
    TicketService ticketService;

    @MockitoBean
    private JwtService jwtService;

    // ---------- Helpers ----------
    private UserDTO.userResponse userResponse(Long id) {
        return new UserDTO.userResponse(
                id,
                "John Doe",
                "john@test.com",
                "3001234567",
                UserRols.DRIVER,
                LocalDate.of(1995, 1, 1)
        );
    }

    private TicketDTO.ticketResponse ticketResponse(Long id) {
        return new TicketDTO.ticketResponse(
                id,
                null,
                null,
                null,
                null,
                BigDecimal.valueOf(222),
                null,
                null,null
        );
    }

    // =====================================================
    @Test
    void get_shouldReturn200() throws Exception {

        Mockito.when(userService.get(5L))
                .thenReturn(userResponse(5L));

        mvc.perform(get("/api/v1/user/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    // =====================================================
    @Test
    void create_shouldReturn201() throws Exception {

        var req = new UserDTO.userCreateRequest(
                "John Doe",
                "john@test.com",
                "3001234567",
                UserRols.DRIVER,
                LocalDate.of(1990, 1, 10)
        );

        var res = userResponse(10L);

        Mockito.when(userService.save(any())).thenReturn(res);

        mvc.perform(post("/api/v1/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)));
    }

    // =====================================================
    @Test
    void getAll_shouldReturn200() throws Exception {

        Page<UserDTO.userResponse> page =
                new PageImpl<>(List.of(userResponse(1L)));

        Mockito.when(userService.getAll(any(Pageable.class)))
                .thenReturn(page);

        mvc.perform(get("/api/v1/user/all")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    // =====================================================
    @Test
    void delete_shouldReturn204() throws Exception {

        mvc.perform(delete("/api/v1/user/delete/7"))
                .andExpect(status().isNoContent());
    }

    // =====================================================
    @Test
    void update_shouldReturn200() throws Exception {

        var req = new UserDTO.userUpdateRequest(
                "New Name",
                "new@test.com",
                "3112223344",
                UserRols.ADMIN
        );

        var res = userResponse(34L);

        Mockito.when(userService.update(any(), eq(34L)))
                .thenReturn(res);

        mvc.perform(patch("/api/v1/user/update/34")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(34));
    }

    // =====================================================
    @Test
    void getTicketsById_shouldReturn200() throws Exception {

        Page<TicketDTO.ticketResponse> page =
                new PageImpl<>(List.of(ticketResponse(7L)));

        Mockito.when(ticketService.getUserTickets(any(Pageable.class), eq(4L)))
                .thenReturn(page);

        mvc.perform(get("/api/v1/user/4/tickets")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(7));
    }

    // =====================================================
    @Test
    void getUsersByRol_shouldReturn200() throws Exception {

        Page<UserDTO.userResponse> page =
                new PageImpl<>(List.of(userResponse(2L)));

        Mockito.when(userService.getUsersByRol(eq(UserRols.DISPATCHER), any(Pageable.class)))
                .thenReturn(page);

        mvc.perform(get("/api/v1/user/find/type")
                        .param("rol", "DISPATCHER")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(2));
    }

    // =====================================================
    @Test
    void countUsers_shouldReturn200() throws Exception {

        Mockito.when(userService.countUsersByRol(UserRols.DRIVER))
                .thenReturn(12);

        mvc.perform(get("/api/v1/user/count/DRIVER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(12));
    }

}
