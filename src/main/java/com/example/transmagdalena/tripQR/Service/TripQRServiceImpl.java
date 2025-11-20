package com.example.transmagdalena.tripQR.Service;

import com.example.transmagdalena.trip.TripStatus;
import com.example.transmagdalena.tripQR.DTO.TripQRDTO;
import com.example.transmagdalena.tripQR.Mapper.TripQRMapper;
import com.example.transmagdalena.tripQR.TripQR;
import com.example.transmagdalena.tripQR.repository.TripQrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TripQRServiceImpl implements  TripQRService {

    private final TripQrRepository tripQrRepository;

    private final TripQRMapper tripQRMapper;

    @Override
    public void delete(TripQR tripQR) {
        tripQrRepository.delete(tripQR);
    }

    @Override
    public Page<TripQRDTO.tripQRResponse> getTripQR(Long tripid, Pageable pageable) {
        return tripQrRepository.getTripQRByTripId(tripid, pageable).map(tripQRMapper::toResponse);
    }
}
