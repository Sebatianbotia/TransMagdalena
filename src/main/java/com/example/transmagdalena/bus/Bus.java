package com.example.transmagdalena.bus;

import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.trip.Trip;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.Where;

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
@SQLDelete(sql = "update buses set is_delete = true where id =?")
@Where(clause = "is_delete = false")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String plate;

    private int capacity;

    private BusStatus status;

    @OneToMany(mappedBy = "bus")
    private List<Trip> trips = new ArrayList<>();

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    private Set<Seat> seats = new HashSet<>();

    private Boolean isDelete;

}
