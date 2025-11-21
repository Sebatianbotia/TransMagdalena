package com.example.transmagdalena.tripQR;

import com.example.transmagdalena.ticket.Ticket;
import com.example.transmagdalena.trip.Trip;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trip_qrs")
@Builder
@SQLDelete(sql = "update trip_qrs set status = 'CANCELLED' where id = ? ")
@Where(clause = "status != 1")
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

    private TriQRStatus status;


    public void setTrip(Trip trip) {
        if (this.trip == trip) return;

        if (this.trip != null) {
            this.trip.getTripQRs().remove(this);
        }

        this.trip = trip;

        if (trip != null) {
            trip.getTripQRs().add(this);
        }
    }

    public void setTicket(Ticket ticket) {
        if (this.ticket == ticket) return;

        Ticket old = this.ticket;
        if (old != null) {
            old.setTripQR(null); // si tu Ticket tiene referencia
        }

        this.ticket = ticket;

        if (ticket != null) {
            ticket.setTripQR(this);
        }
    }


}
