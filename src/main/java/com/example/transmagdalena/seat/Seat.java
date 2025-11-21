package com.example.transmagdalena.seat;


import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.seatHold.SeatHold;
import com.example.transmagdalena.trip.Trip;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "seats")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SQLDelete(sql = "update seats set is_delete = true where id =?")
@Where(clause = "is_delete = false")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Integer number;

    private SeatType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "busId")
    private Bus bus;

    @OneToMany(mappedBy = "seat")
    private Set<SeatHold> seatHolds = new HashSet<>();

    private Boolean isDelete;

    public void setBus(Bus bus) {
        if (this.bus == bus){return;}

        Bus oldBus = this.bus;
        if (oldBus != null) {
            oldBus.getSeats().remove(this);
        }
        this.bus = bus;
        if (this.bus != null) {
            this.bus.getSeats().add(this);
        }
    }

}
