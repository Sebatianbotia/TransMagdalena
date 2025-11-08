package com.example.transmagdalena.stop;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.ticket.Ticket;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stops")
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
