package com.example.transmagdalena.fareRule;

import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.trip.Trip;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "fare_rules")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FareRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToMany(mappedBy = "fareRule")
    private Set<RouteStop> routeStops;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "is_dynamic_pricing", nullable = false)
    private Boolean isDynamicPricing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_stop_id")
    private Stop origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_stop_id")
    private Stop destination;

    @OneToMany(mappedBy = "fareRule")
    private Set<Trip> trips;

    public void setOrigin(Stop origin) {
        if (this.origin == origin){return;}

        Stop ori = this.getOrigin();
        if (ori != null){
            ori.getOriginFareRules().remove(this);
        }
        this.origin = origin;
        if (this.origin != null){
            this.origin.getOriginFareRules().add(this);
        }

    }

    public void setDestination(Stop destination) {
        if (this.destination == destination){return;}
        Stop dest = this.getDestination();
        if (dest != null){
            dest.getDestinationFareRules().remove(this);
        }
        this.destination = destination;
        if (this.destination != null){
            this.destination.getDestinationFareRules().add(this);
        }
    }



}
