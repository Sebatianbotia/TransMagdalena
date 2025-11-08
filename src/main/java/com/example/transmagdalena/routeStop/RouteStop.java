package com.example.transmagdalena.routeStop;

import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.stop.Stop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routeStops")
public class RouteStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int stopOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeId")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originId")
    private Stop origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinationId")
    private Stop destination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fareRuleId")
    private FareRule fareRule;
}
