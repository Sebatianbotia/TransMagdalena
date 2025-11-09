package com.example.transmagdalena.baggage.repository;

import com.example.transmagdalena.baggage.Baggage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaggageRepository extends JpaRepository<Baggage, Long> {
    Optional<Baggage> findByTagCode(String tagCode);
}
