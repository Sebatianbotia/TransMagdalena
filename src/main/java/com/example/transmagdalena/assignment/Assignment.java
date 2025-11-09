package com.example.transmagdalena.assignment;


import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "assignments")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driverId")
    private User driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatcherId")
    private User dispatcher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tripId")
    private Trip trip;



    private boolean checkList;

    private OffsetDateTime assignedAt;

}
