package com.example.transmagdalena.seatHold;

import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.ticket.Ticket;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "seat_holds")
public class SeatHold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private OffsetDateTime expiresAt =  OffsetDateTime.now().plusMinutes(10);

    private SeatHoldStatus status;

    @OneToOne(mappedBy = "seatHold")
    private Ticket ticket;

    public void addSeat(Seat seat){
        this.seat = seat;
        seat.getSeatHolds().add(this);
    }

    public void removeSeat(Seat seat){
        this.seat = null;
        seat.getSeatHolds().remove(this);
    }

    public void addUser(User user){
        this.user = user;
        user.getSeatHolds().add(this);
    }

    public void removeUser(User user){
        this.user = null;
        user.getSeatHolds().remove(this);
    }

    public void addTrip(Trip trip){
        this.trip = trip;
        trip.getSeatHolds().add(this);
    }

    public void removeTrip(Trip trip){
        this.trip = null;
        trip.getSeatHolds().remove(this);
    }

}
