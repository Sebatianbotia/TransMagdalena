package com.example.transmagdalena.parcel.Controller;

import com.example.transmagdalena.parcel.DTO.ParcelDTO;
import com.example.transmagdalena.parcel.ParcelStatus;
import com.example.transmagdalena.parcel.controller.ParcelController;
import com.example.transmagdalena.parcel.service.ParcelService;
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

@WebMvcTest(ParcelController.class)
@AutoConfigureMockMvc(addFilters = false)
class ParcelControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private ParcelService parcelService;

    @MockitoBean
    private JwtService jwtService;


    private ParcelDTO.parcelResponse response(Long id) {
        return new ParcelDTO.parcelResponse(
                id,
                "TAG001",
                "John",
                "111",
                "Mary",
                "222",
                BigDecimal.valueOf(30000),
                ParcelStatus.CREATED,
                null,
                null
        );
    }

    // ----------------------------------------------------------
    // GET /api/v1/parcel/{id}
    // ----------------------------------------------------------

    @Test
    void getById_shouldReturn200() throws Exception {

        var res = response(1L);

        when(parcelService.get(1L)).thenReturn(res);

        mvc.perform(get("/api/v1/parcel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("TAG001"))
                .andExpect(jsonPath("$.price").value(30000));

        verify(parcelService).get(1L);
    }

    // ----------------------------------------------------------
    // GET /api/v1/parcel/tagCode/{tagCode}
    // ----------------------------------------------------------

    @Test
    void getByTagCode_shouldReturn200() throws Exception {

        var res = response(5L);

        when(parcelService.get("TAG001")).thenReturn(res);

        mvc.perform(get("/api/v1/parcel/tagCode/TAG001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.senderName").value("John"));

        verify(parcelService).get("TAG001");
    }

    // ----------------------------------------------------------
    // GET /api/v1/parcel/all
    // ----------------------------------------------------------

    @Test
    void getAll_shouldReturnPage() throws Exception {

        var res = response(3L);

        Page<ParcelDTO.parcelResponse> page =
                new PageImpl<>(List.of(res));

        when(parcelService.getAll(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/v1/parcel/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(3))
                .andExpect(jsonPath("$.content[0].code").value("TAG001"));

        verify(parcelService).getAll(any(PageRequest.class));
    }

    // ----------------------------------------------------------
    // POST /api/v1/parcel/create
    // ----------------------------------------------------------

    @Test
    void create_shouldReturn201() throws Exception {

        var req = new ParcelDTO.parcelCreateRequest(
                "TAG001",
                "John",
                "111",
                "Mary",
                "222",
                BigDecimal.valueOf(30000),
                ParcelStatus.CREATED,
                null,
                null
        );

        var res = response(10L);

        when(parcelService.save(any())).thenReturn(res);

        mvc.perform(post("/api/v1/parcel/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/api/v1/parcel/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.code").value("TAG001"));

        verify(parcelService).save(any());
    }

    // ----------------------------------------------------------
    // PATCH /api/v1/parcel/update/{id}
    // ----------------------------------------------------------

    @Test
    void update_shouldReturn200() throws Exception {

        var req = new ParcelDTO.parcelUpdateRequest(
                "TAG999",
                "Carlos",
                "999",
                "Ana",
                "888",
                BigDecimal.valueOf(50000),
                com.example.transmagdalena.parcel.ParcelStatus.DELIVERED,
                null,
                null
        );

        var res = new ParcelDTO.parcelResponse(
                1L,
                "TAG999",
                "Carlos",
                "999",
                "Ana",
                "888",
                BigDecimal.valueOf(50000),
                com.example.transmagdalena.parcel.ParcelStatus.DELIVERED,
                null,
                null
        );

        when(parcelService.update(any(), eq(1L))).thenReturn(res);

        mvc.perform(patch("/api/v1/parcel/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("TAG999"))
                .andExpect(jsonPath("$.price").value(50000));

        verify(parcelService).update(any(), eq(1L));
    }
}
