package com.example.transmagdalena.fareRule.repository;

import com.example.transmagdalena.fareRule.FareRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FareRuleRepository extends JpaRepository<FareRule, Long> {

    Optional<FareRule> findById(Long fareRuleId);
}
