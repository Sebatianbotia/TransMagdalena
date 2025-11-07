package com.example.transmagdalena.ticket;

import com.example.transmagdalena.stop.Stop;
import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tripId")
    private Trip trip;

    private Integer seatNumber;//cambiar por seatHold

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

    private String qrCodeUrl;




}
