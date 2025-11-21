package com.example.transmagdalena.tripQR.Service;

import com.example.transmagdalena.trip.TripStatus;
import com.example.transmagdalena.tripQR.DTO.TripQRDTO;
import com.example.transmagdalena.tripQR.TripQR;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TripQRService {

    void delete(TripQR tripQR);
    Page<TripQRDTO.tripQRResponse> getTripQR(Long tripid, Pageable pageable);
}
