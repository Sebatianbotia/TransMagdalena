package com.example.transmagdalena.fareRule;

import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.RouteStop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "fare_rules")
public class FareRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    // hay que revisar esta tabla, la relacion que tiene con los stops

    @OneToMany(mappedBy = "fareRule")
    private Set<RouteStop> routeStops;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "is_dynamic_pricing", nullable = false)
    private boolean isDynamicPricing;
}
