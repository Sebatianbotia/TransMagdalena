package com.example.transmagdalena.seat;


import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.trip.Trip;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "seats")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private int number;

    private SeatType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "busId")
    private Bus bus;

    @OneToMany(mappedBy = "seat")
    private Set<SeatHold> seatHolds;

    public void addBus(Bus bus) {
        this.bus = bus;
        bus.getSeats().add(this);
    }
    public void removeBus(Bus bus) {
        this.bus = null;
        bus.getSeats().remove(this);
    }

}
