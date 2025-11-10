package com.example.transmagdalena.routeStop;

import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.stop.Stop;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routeStops")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RouteStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private int stopOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeId")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_Id")
    private Stop origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_Id")
    private Stop destination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fare_rule_Id")
    private FareRule fareRule;


    public void addRoute(Route route) {
        if(this.route != null) {
            this.route.getRouteStops().remove(this);
        }
        this.route = route;
        route.getRouteStops().add(this);
    }

    public void addOrigin(Stop origin) {
        if(this.origin != null) {
            this.origin.getOriginRouteStops().remove(this);
        }
        this.origin = origin;
        origin.getOriginRouteStops().add(this);
    }
    public void addDestination(Stop destination) {
        if(this.destination != null) {
            this.destination.getDestinationRouteStops().remove(this);
        }
        this.destination = destination;
        destination.getDestinationRouteStops().add(this);
    }
}
