package com.example.transmagdalena.tripQR.Controller;

import com.example.transmagdalena.tripQR.Service.TripQRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/tripQr")
@Validated
@RequiredArgsConstructor
public class TripQRController {

    private final TripQRService tripQRService;

//    @DeleteMapping("/cancel/{id}")
//    public ResponseEntity<?> cancelTrip(@PathVariable("id") Integer id) {
//        return tripQRService.delete();
//    }
}
