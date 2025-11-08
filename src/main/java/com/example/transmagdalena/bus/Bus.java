package com.example.transmagdalena.bus;

import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.trip.Trip;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "buses")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plate;

    private int capacity;

    private String status;

    @OneToMany(mappedBy = "bus")
    private List<Trip> trips;

    @OneToMany(mappedBy = "bus")
    private Set<Seat> seats;

}
