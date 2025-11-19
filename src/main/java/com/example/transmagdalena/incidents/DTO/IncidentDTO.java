package com.example.transmagdalena.incidents.DTO;

import com.example.transmagdalena.configuration.Configuration;
import com.example.transmagdalena.incidents.EntityType;
import com.example.transmagdalena.incidents.IncidentType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class IncidentDTO {

    public record incidentCreateRequest(IncidentType incidentType, Long entityId, EntityType entityType,
                                        String note, @JsonFormat(pattern = Configuration.DATE_FORMAT) OffsetDateTime createdAt) implements Serializable{
    }

    public record incidentUpdateRequest(IncidentType incidentType, Long entityId, EntityType entityType,
                                        String note, @JsonFormat(pattern = Configuration.DATE_FORMAT) OffsetDateTime createdAt) implements Serializable{}


    public record incidentResponse(Long id, IncidentType incidentType, Long entityId, EntityType entityType,
                                   String note, @JsonFormat(pattern = Configuration.DATE_FORMAT) OffsetDateTime createdAt) implements Serializable{}
}
