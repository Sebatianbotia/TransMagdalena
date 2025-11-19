package com.example.transmagdalena.incidents.service;

import com.example.transmagdalena.incidents.DTO.IncidentDTO;
import com.example.transmagdalena.incidents.EntityType;
import com.example.transmagdalena.incidents.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IncidentService {
    IncidentDTO.incidentResponse save(IncidentDTO.incidentCreateRequest request);
    Page<IncidentDTO.incidentResponse> getIncidentByIdAndEntityType(Long entityId, EntityType entityType, Pageable pageable);
    IncidentDTO.incidentResponse update(IncidentDTO.incidentUpdateRequest request, Long id);

}
