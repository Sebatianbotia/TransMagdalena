package com.example.transmagdalena.bus;

import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.trip.Trip;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "buses")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String plate;

    private int capacity;

    private String status;

    @OneToMany(mappedBy = "bus")
    private List<Trip> trips = new ArrayList<>();

    @OneToMany(mappedBy = "bus")
    private Set<Seat> seats = new HashSet<>();

}
