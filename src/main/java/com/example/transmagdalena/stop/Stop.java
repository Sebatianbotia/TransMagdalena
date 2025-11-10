package com.example.transmagdalena.stop;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.ticket.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stops")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;


    private float lat;

    private float lng;

    @OneToMany(mappedBy = "origin")
    private List<RouteStop> originRouteStops;

    @OneToMany(mappedBy = "destination")
    private List<RouteStop> destinationRouteStops;

    @OneToMany(mappedBy = "origin")
    private Set<Ticket> originTickets;

    @OneToMany(mappedBy = "destination")
    private Set<Ticket> destinationTickets;

    @OneToMany(mappedBy = "origin")
    private Set<Route> originRoutes;

    @OneToMany(mappedBy = "destination")
    private Set<Route> destinationRoutes;

    @OneToMany(mappedBy = "origin")
    private Set<FareRule> originFareRules;

    @OneToMany(mappedBy = "destination")
    private Set<FareRule> destinationFareRules;

    public void addCity(City city) {
        this.city = city;
        city.getStops().add(this);
    }

    public void removeCity(City city) {
        this.city = null;
        city.getStops().remove(this);
    }

}
