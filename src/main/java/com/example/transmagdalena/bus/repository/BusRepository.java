package com.example.transmagdalena.bus.repository;

import com.example.transmagdalena.bus.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Integer> {

    Optional<Bus> findBusById(Long id);
    long count();

}


