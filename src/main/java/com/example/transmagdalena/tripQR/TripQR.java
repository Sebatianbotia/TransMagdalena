package com.example.transmagdalena.tripQR;

import com.example.transmagdalena.ticket.Ticket;
import com.example.transmagdalena.trip.Trip;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trip_qrs")
@Builder
public class TripQR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(nullable = false, unique = true)
    private String qrSeed;


    public void setTrip(Trip trip) {
        trip.getTripQRs().add(this);
        this.trip = trip;
    }

    public void removeTrip(Trip trip) {
        trip.getTripQRs().remove(this);
        this.trip = null;
    }


}
