package com.example.transmagdalena.stop;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.ticket.Ticket;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stops")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SQLDelete(sql = "update stops set is_delete = true where id = ? ")
@Where(clause = "is_delete = false")
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    private Float lat;

    private Float lng;

    @OneToMany(mappedBy = "origin")
    private List<RouteStop> originRouteStops = new ArrayList<>();

    @OneToMany(mappedBy = "destination")
    private List<RouteStop> destinationRouteStops = new ArrayList<>();

    @OneToMany(mappedBy = "origin")
    private Set<Ticket> originTickets = new HashSet<>();

    @OneToMany(mappedBy = "destination")
    private Set<Ticket> destinationTickets = new HashSet<>();

    @OneToMany(mappedBy = "origin")
    private Set<Route> originRoutes = new HashSet<>();

    @OneToMany(mappedBy = "destination")
    private Set<Route> destinationRoutes = new HashSet<>();

    @OneToMany(mappedBy = "origin")
    private Set<FareRule> originFareRules = new HashSet<>();

    @OneToMany(mappedBy = "destination")
    private Set<FareRule> destinationFareRules = new HashSet<>();

    private Boolean isDelete;

    public void setCity(City city) {
        if (city == this.city) {return;}
        if (this.city != null) {
            this.city.getStops().remove(this);
        }
        this.city = city;
        this.city.getStops().add(this);
    }

}
