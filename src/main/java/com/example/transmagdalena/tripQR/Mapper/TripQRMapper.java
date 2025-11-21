package com.example.transmagdalena.tripQR.Mapper;

import com.example.transmagdalena.tripQR.DTO.TripQRDTO;
import com.example.transmagdalena.tripQR.TripQR;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripQRMapper {

    TripQRDTO.tripQRResponse toResponse(TripQR tripQR);
}
