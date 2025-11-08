package com.example.transmagdalena.seat;


import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    private SeatType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "busId")
    private Bus bus;

    @OneToMany(mappedBy = "seat")
    private Set<SeatHold> seatHolds;

}
