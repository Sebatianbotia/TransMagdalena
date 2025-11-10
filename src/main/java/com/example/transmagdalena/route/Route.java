package com.example.transmagdalena.route;

import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.trip.Trip;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "routes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_id")
    private Stop origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private Stop destination;

    @OneToMany(mappedBy = "route")
    private List<Trip> trips = new ArrayList<>();

    @OneToMany(mappedBy = "route")
    private List<RouteStop> routeStops  = new ArrayList<>();

    public void addOrigin(Stop origin) {
        this.origin = origin;
        origin.getOriginRoutes().add(this);
    }

    public void addDestination(Stop destination) {
        this.destination = destination;
        destination.getDestinationRoutes().add(this);
    }


}
