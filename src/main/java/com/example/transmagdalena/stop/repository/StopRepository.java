package com.example.transmagdalena.stop.repository;

import com.example.transmagdalena.stop.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StopRepository extends JpaRepository<Stop, Long> {

    Optional<Stop> findByNameIgnoreCase(String name);
    List<Stop> findStStopsByCityId(Long cityId);
}
