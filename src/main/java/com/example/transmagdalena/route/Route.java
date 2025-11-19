package com.example.transmagdalena.route;

import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.trip.Trip;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "routes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SQLDelete(sql = "update routes set is_delete = true where id = ?")
@Where(clause = "is_delete = false")
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
    private List<Trip> trips;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<RouteStop> routeStops;

    private Float distanceKm;

    private Float durationTime;

    private Boolean isDelete;

    public void addOrigin(Stop origin) {
        this.origin = origin;
        origin.getOriginRoutes().add(this);
    }

    public void addDestination(Stop destination) {
        this.destination = destination;
        destination.getDestinationRoutes().add(this);
    }


    public void setOrigin(Stop origin) {
        if (origin == this.origin) {return;}
        if (this.origin != null){
            this.origin.getOriginRoutes().remove(this);
        }
        this.origin = origin;
        this.origin.getOriginRoutes().add(this);
    }

    public void setDestination(Stop destination) {
        if (destination == this.destination) {return;}
        if (this.destination != null){
            this.destination.getDestinationRoutes().remove(this);
        }
        this.destination = destination;
        this.destination.getDestinationRoutes().add(this);
    }


}
