package com.example.transmagdalena.trip;

import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.ticket.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "trips")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "busId")
    private Bus bus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeId")
    private Route route;

    @Column(nullable = false)
    private OffsetDateTime date;

    @Column(nullable = false)
    private OffsetDateTime departureAt;

    @Column(nullable = false)
    private OffsetDateTime arrivalAt;

    @Column(nullable = false)
    private TripStatus tripStatus;

    @OneToMany(mappedBy = "trip")
    private Set<SeatHold> seatHolds;

    @OneToMany(mappedBy = "trip")
    private Set<Ticket> tickets;

    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY)
    private Set<Assignment> dispatcherAssignments;



}
