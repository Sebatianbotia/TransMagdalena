package com.example.transmagdalena.incidents.repository;

import com.example.transmagdalena.incidents.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident,Long> {
}
