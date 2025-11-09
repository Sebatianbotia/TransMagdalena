package com.example.transmagdalena.parcel;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "parcels")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String code;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false)
    private String senderPhone;

    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = false)
    private String receiverPhone;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private ParcelStatus status;

    private String proofPhotoUrl;

    private Integer deliveryOtp;
}
