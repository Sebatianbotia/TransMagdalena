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
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatcher_id")
    private User dispatcher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tripId")
    private Trip trip;

    private Boolean checkList;

    private OffsetDateTime assignedAt;

    public void setDriver(User driver) {
        if (driver == this.driver) {return;}
        User oldDriver = this.driver;
        if (oldDriver != null){
            oldDriver.getDriverAssignments().remove(this);
        }
        this.driver = driver;
        if (this.driver != null) {
            this.driver.getDriverAssignments().add(this);
        }
    }

    public void setDispatcher(User dispatcher) {
        if (dispatcher == this.dispatcher) {return;}
        User oldDispatcher = this.dispatcher;
        if (oldDispatcher != null){
            oldDispatcher.getDispatcherAssignments().remove(this);
        }
        this.dispatcher = dispatcher;
        if (this.dispatcher != null) {
            this.dispatcher.getDispatcherAssignments().add(this);
        }
    }

    public void setTrip(Trip trip) {
        if (trip == this.trip) {return;}
        Trip oldTrip = this.trip;
        if (oldTrip != null){
            oldTrip.getAssignments().remove(this);
        }
        this.trip = trip;
        if (this.trip != null) {
            this.trip.getAssignments().add(this);
        }
    }

}