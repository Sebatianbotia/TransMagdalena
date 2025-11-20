package com.example.transmagdalena.incidents;

import com.example.transmagdalena.configuration.Configuration;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "incidents")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private EntityType entityType;

    private Long entityId;

    private IncidentType incidentType;

    private String note;

    @JsonFormat(pattern = Configuration.DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

}
