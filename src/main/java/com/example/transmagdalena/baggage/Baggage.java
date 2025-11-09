package com.example.transmagdalena.baggage;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "baggages")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Baggage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Integer weight;

    private String tagCode;

    private BigDecimal fee;
}
