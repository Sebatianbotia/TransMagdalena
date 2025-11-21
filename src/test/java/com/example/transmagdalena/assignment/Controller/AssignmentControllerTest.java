package com.example.transmagdalena.assignment.Controller;

import com.example.transmagdalena.assignment.DTO.AssignmentDTO;
import com.example.transmagdalena.assignment.service.AssignmentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssignmentController.class)
@AutoConfigureMockMvc(addFilters = false) // ← ESTA ES LA SOLUCIÓN FÁCIL
class AssignmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private AssignmentServiceImpl assignmentService;

    @MockitoBean
    private com.example.transmagdalena.Security.Config.jwt.JwtService jwtService;

    private AssignmentDTO.userDTO createUserDTO() {
        return new AssignmentDTO.userDTO("John Doe", "john@email.com", "123456789", com.example.transmagdalena.user.UserRols.DRIVER);
    }

    private AssignmentDTO.tripDTO createTripDTO() {
        return new AssignmentDTO.tripDTO(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
    }

    private AssignmentDTO.assignmentResponse createAssignmentResponse(Long id) {
        return new AssignmentDTO.assignmentResponse(id, createUserDTO(), createUserDTO(), createTripDTO());
    }

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        // Given
        var createRequest = new AssignmentDTO.assignmentCreateRequest(
                1L, 2L, 3L, true, OffsetDateTime.now()
        );
        var response = createAssignmentResponse(10L);

        when(assignmentService.save(any(AssignmentDTO.assignmentCreateRequest.class))).thenReturn(response);

        // When & Then
        mvc.perform(post("/api/v1/assignment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/assignment/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.driver.name").value("John Doe"))
                .andExpect(jsonPath("$.dispatcher.email").value("john@email.com"));

        verify(assignmentService).save(any(AssignmentDTO.assignmentCreateRequest.class));
    }

    @Test
    void get_shouldReturn200() throws Exception {
        // Given
        var response = createAssignmentResponse(5L);
        when(assignmentService.get(5L)).thenReturn(response);

        // When & Then
        mvc.perform(get("/api/v1/assignment/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.trip.id").value(1))
                .andExpect(jsonPath("$.trip.arrivalAt").value("10:00:00"))
                .andExpect(jsonPath("$.trip.departureAt").value("12:00:00"));

        verify(assignmentService).get(5L);
    }

    // ... el resto de tus tests igual
}