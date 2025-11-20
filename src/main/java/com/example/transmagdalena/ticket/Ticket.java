package com.example.transmagdalena.ticket;

import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.tripQR.TripQR;
import com.example.transmagdalena.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tickets")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SQLDelete(sql = "update tickets set status = 'CANCELLED' where id = ? ")
@Where(clause = "status != 1 ")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seathold_id")
    private SeatHold seatHold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originId")
    private Stop origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinationId")
    private Stop destination;

    private BigDecimal price;

    private TicketPaymentMethod paymentMethod;

    private TicketStatus status;

    @Column(name = "ticket_code", unique = true, nullable = false, length = 20)
    private String ticketCode;

    private String qrCodeUrl;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private TripQR tripQR;

    public void  addTrip(Trip trip) {
        if (this.trip != null) {
            this.trip.getTickets().remove(this);
        }
        this.trip = trip;
        trip.getTickets().add(this);
    }

    public void addSeatHold(SeatHold seatHold) {
        this.seatHold = seatHold;
        seatHold.setTicket(this);
    }
    public void addUser(User user) {
        if(this.user != null) {
            this.user.getTickets().remove(this);
        }
        this.user = user;
        user.getTickets().add(this);
    }

    public void addDestination(Stop destination) {
        if(this.destination != null) {
            this.destination.getDestinationTickets().remove(this);
        }
        this.destination = destination;
        destination.getDestinationTickets().add(this);
    }

    public void addOrigin(Stop origin) {
        if(this.origin != null) {
            this.origin.getDestinationTickets().remove(this);
        }
        this.origin = origin;
        origin.getDestinationTickets().add(this);
    }




}
