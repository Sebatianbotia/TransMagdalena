package com.example.transmagdalena.user;

import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.seatHold.SeatHold;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;
    private String email;

    private UserRols rols;

    private String passwordHash;

    @Column(nullable = false) // este valor debe generarse al crearse la cuenta
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<Assignment> driverAssignments;

    @OneToMany(mappedBy = "dispatcher", fetch = FetchType.LAZY)
    private Set<Assignment> dispatcherAssignments;

    @OneToMany(mappedBy = "user")
    private Set<SeatHold> seatHolds;

    public void addDriverAssignment(Assignment assignment) {
        driverAssignments.add(assignment);
        assignment.setDriver(this);
    }

    public void removeDriverAssignment(Assignment assignment) {
        driverAssignments.remove(assignment);
        assignment.setDriver(null);
    }

    public void addDispatcherAssignment(Assignment assignment) {
        dispatcherAssignments.add(assignment);
        assignment.setDispatcher(this);
    }

    public void removeDispatcherAssignment(Assignment assignment) {
        dispatcherAssignments.remove(assignment);
        assignment.setDispatcher(null);
    }
}
