package com.example.transmagdalena.city;

import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.weather.Weather;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cities")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private float lat;

    private float lon;

    @OneToMany(mappedBy = "city")
    private Set<Stop> stops = new HashSet<>();

    @OneToMany(mappedBy = "city")
    private Set<Weather> weathers = new HashSet<>();

}
