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

    private OffsetDateTime expiresAt;

    private SeatHoldStatus status;

    @OneToOne(mappedBy = "seatHold")
    private Ticket ticket;

    public void setSeat(Seat seat) {
        if (this.seat == seat){return;}
        Seat oldSeat = this.seat;
        if (oldSeat != null){
            oldSeat.getSeatHolds().remove(this);
        }
        this.seat = seat;
        if (this.seat != null){
            this.seat.getSeatHolds().add(this);
        }
    }

    public void setUser(User user) {
        if (this.user == user){return;}
        User oldUser = this.user;
        if (oldUser != null){
            oldUser.getSeatHolds().remove(this);
        }
        this.user = user;
        if (this.user != null){
            this.user.getSeatHolds().add(this);
        }
    }

    public void setTrip(Trip trip) {
        if (this.trip == trip){return;}
        Trip oldTrip = this.trip;
        if (oldTrip != null){
            oldTrip.getSeatHolds().remove(this);
        }
        this.trip = trip;
        if (this.trip != null){
            this.trip.getSeatHolds().add(this);
        }
    }

}
