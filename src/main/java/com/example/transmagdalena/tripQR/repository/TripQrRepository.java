package com.example.transmagdalena.tripQR.repository;

import com.example.transmagdalena.trip.Trip;
import com.example.transmagdalena.tripQR.TripQR;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TripQrRepository extends JpaRepository<TripQR, Long> {

    Page<TripQR> getTripQRByTripId(Long tripId, Pageable pageable);

    Optional<TripQR> findTripQRByQrSeed(String qrSeed);


}
