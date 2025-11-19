package com.example.transmagdalena.incidents.service;

import com.example.transmagdalena.incidents.DTO.IncidentDTO;
import com.example.transmagdalena.incidents.EntityType;
import com.example.transmagdalena.incidents.Incident;
import com.example.transmagdalena.incidents.Mapper.IncidentMapper;
import com.example.transmagdalena.incidents.repository.IncidentRepository;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;

    @Override
    @Transactional
    public IncidentDTO.incidentResponse save(IncidentDTO.incidentCreateRequest request) {
        var s = incidentMapper.toEntity(request);
        return incidentMapper.toDTO(incidentRepository.save(s));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IncidentDTO.incidentResponse> getIncidentByIdAndEntityType(Long entityId, EntityType entityType, Pageable pageable) {
        var s = incidentRepository.findAllByEntityTypeAndEntityId(entityType, entityId, pageable);
        return s.map(incidentMapper::toDTO);
    }

    @Override
    @Transactional
    public IncidentDTO.incidentResponse update(IncidentDTO.incidentUpdateRequest request, Long id) {
        var s = getObject(id);
        incidentMapper.update(request,s);
        return incidentMapper.toDTO(s);
    }

    public Page<IncidentDTO.incidentResponse> getAll(Pageable pageable) {
        return incidentRepository.findAll(pageable).map(incidentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Incident getObject(Long id){
        return incidentRepository.findById(id).orElseThrow(() -> new NotFoundException("incident not found"));
    }
}
