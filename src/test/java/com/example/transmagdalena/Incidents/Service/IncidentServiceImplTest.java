package com.example.transmagdalena.Incidents.Service;

import com.example.transmagdalena.incidents.DTO.IncidentDTO;
import com.example.transmagdalena.incidents.EntityType;
import com.example.transmagdalena.incidents.Incident;
import com.example.transmagdalena.incidents.IncidentType;
import com.example.transmagdalena.incidents.Mapper.IncidentMapper;
import com.example.transmagdalena.incidents.repository.IncidentRepository;
import com.example.transmagdalena.incidents.service.IncidentServiceImpl;
import com.example.transmagdalena.utilities.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceImplTest {

    @Mock
    IncidentRepository incidentRepository;

    @Mock
    IncidentMapper incidentMapper;

    @InjectMocks
    IncidentServiceImpl service;

    @Test
    void shouldCreateIncident() {
        var now = LocalDateTime.now();

        var req = new IncidentDTO.incidentCreateRequest(
                IncidentType.DELIVERY_FAIL,
                20L,
                EntityType.TRIP,
                "Motor failure",
                now
        );

        var entity = Incident.builder()
                .incidentType(IncidentType.DELIVERY_FAIL)
                .entityId(20L)
                .entityType(EntityType.TRIP)
                .note("Motor failure")
                .createdAt(now)
                .build();

        when(incidentMapper.toEntity(req)).thenReturn(entity);

        when(incidentRepository.save(entity)).thenAnswer(inv -> {
            Incident i = inv.getArgument(0);
            i.setId(100L);
            return i;
        });

        var resDto = new IncidentDTO.incidentResponse(
                100L, IncidentType.DELIVERY_FAIL, 20L, EntityType.TRIP, "Motor failure", now
        );

        when(incidentMapper.toDTO(any())).thenReturn(resDto);

        var result = service.save(req);

        assertThat(result.id()).isEqualTo(100L);
        assertThat(result.note()).isEqualTo("Motor failure");
        verify(incidentRepository).save(entity);
    }

    @Test
    void shouldUpdateIncident() {
        var now = LocalDateTime.now();

        var existing = Incident.builder()
                .id(5L)
                .incidentType(IncidentType.DELIVERY_FAIL)
                .entityId(10L)
                .entityType(EntityType.TRIP)
                .note("Old note")
                .createdAt(now)
                .build();

        when(incidentRepository.findById(5L)).thenReturn(Optional.of(existing));

        var req = new IncidentDTO.incidentUpdateRequest(
                IncidentType.DELIVERY_FAIL,
                10L,
                EntityType.TRIP,
                "Updated note",
                now
        );

        doAnswer(inv -> {
            var update = (IncidentDTO.incidentUpdateRequest) inv.getArgument(0);
            var ent = (Incident) inv.getArgument(1);
            ent.setIncidentType(update.incidentType());
            ent.setNote(update.note());
            return null;
        }).when(incidentMapper).update(eq(req), eq(existing));

        var res = new IncidentDTO.incidentResponse(
                5L,
                IncidentType.DELIVERY_FAIL,
                10L,
                EntityType.TRIP,
                "Updated note",
                now
        );

        when(incidentMapper.toDTO(existing)).thenReturn(res);

        var result = service.update(req, 5L);

        assertThat(result.note()).isEqualTo("Updated note");
        assertThat(result.incidentType()).isEqualTo(IncidentType.DELIVERY_FAIL);
        verify(incidentMapper).update(req, existing);
    }

    @Test
    void shouldReturnPagedByEntityTypeAndEntityId() {
        var now = LocalDateTime.now();

        var entity = Incident.builder()
                .id(1L)
                .incidentType(IncidentType.DELIVERY_FAIL)
                .entityId(5L)
                .entityType(EntityType.TRIP)
                .createdAt(now)
                .build();

        var page = new PageImpl<>(java.util.List.of(entity));

        when(incidentRepository.findAllByEntityTypeAndEntityId(
                EntityType.TRIP, 5L, PageRequest.of(0, 10)
        )).thenReturn(page);

        var dto = new IncidentDTO.incidentResponse(
                1L, IncidentType.DELIVERY_FAIL, 5L, EntityType.TRIP, null, now
        );

        when(incidentMapper.toDTO(entity)).thenReturn(dto);

        var result = service.getIncidentByIdAndEntityType(5L, EntityType.TRIP, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).incidentType()).isEqualTo(IncidentType.DELIVERY_FAIL);
    }

    @Test
    void shouldReturnPagedIncidents() {
        var now = LocalDateTime.now();

        var entity = Incident.builder()
                .id(3L)
                .incidentType(IncidentType.DELIVERY_FAIL)
                .createdAt(now)
                .build();

        var page = new PageImpl<>(java.util.List.of(entity));

        when(incidentRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);

        var dto = new IncidentDTO.incidentResponse(
                3L, IncidentType.DELIVERY_FAIL, null, null, null, now
        );

        when(incidentMapper.toDTO(entity)).thenReturn(dto);

        var result = service.getAll(PageRequest.of(0, 5));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(3L);
    }

    @Test
    void shouldThrowWhenIncidentNotFound() {
        when(incidentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getObject(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("incident not found");
    }

    @Test
    void shouldReturnIncidentObject() {
        var incident = Incident.builder()
                .id(7L)
                .incidentType(IncidentType.DELIVERY_FAIL)
                .build();

        when(incidentRepository.findById(7L)).thenReturn(Optional.of(incident));

        var result = service.getObject(7L);

        assertThat(result.getId()).isEqualTo(7L);
        verify(incidentRepository).findById(7L);
    }
}
