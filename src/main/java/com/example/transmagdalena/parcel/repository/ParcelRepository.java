package com.example.transmagdalena.parcel.repository;

import com.example.transmagdalena.parcel.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    Optional<Parcel> findByCode(String code);
}
