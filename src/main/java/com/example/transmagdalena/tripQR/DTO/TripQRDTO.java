package com.example.transmagdalena.tripQR.DTO;

import com.example.transmagdalena.tripQR.TriQRStatus;

import java.io.Serializable;

public class TripQRDTO {

    public record tripQRResponse(
            Long id, String qrSeed, TriQRStatus status
    )implements Serializable {}

}
