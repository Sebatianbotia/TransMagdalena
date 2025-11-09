package com.example.transmagdalena.stop.repository;

import com.example.transmagdalena.stop.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StopRepository extends JpaRepository<Stop, Long> {

    Optional<Stop> findByName(String name);
}
