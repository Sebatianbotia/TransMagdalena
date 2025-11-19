package com.example.transmagdalena.incidents.Mapper;

import com.example.transmagdalena.incidents.DTO.IncidentDTO;
import com.example.transmagdalena.incidents.Incident;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IncidentMapper {


Incident toEntity(IncidentDTO.incidentCreateRequest request);

void update(IncidentDTO.incidentUpdateRequest request, @MappingTarget Incident incident);

IncidentDTO.incidentResponse toDTO(Incident incident);

}
