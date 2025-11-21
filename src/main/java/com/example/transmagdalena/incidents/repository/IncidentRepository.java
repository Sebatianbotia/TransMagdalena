package com.example.transmagdalena.incidents.repository;

import com.example.transmagdalena.incidents.EntityType;
import com.example.transmagdalena.incidents.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident,Long> {

    public Page<Incident> findAllByEntityTypeAndEntityId(EntityType entityType, Long entityId, Pageable pageable);
}
