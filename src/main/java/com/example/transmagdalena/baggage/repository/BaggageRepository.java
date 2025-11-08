package com.example.transmagdalena.baggage.repository;

import com.example.transmagdalena.baggage.Baggage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaggageRepository extends JpaRepository<Baggage, Long> {
}
