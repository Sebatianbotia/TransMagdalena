package com.example.transmagdalena.tripQR.Mapper;

import com.example.transmagdalena.tripQR.DTO.TripQRDTO;
import com.example.transmagdalena.tripQR.TripQR;
import com.example.transmagdalena.tripQR.TriQRStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class TripQRMapperTest {
    private final TripQRMapper tripQRMapper = Mappers.getMapper(TripQRMapper.class);

    @Test
    void testToResponse() {
        TripQR tripQR = TripQR.builder()
                .id(1L)
                .qrSeed("QR_SEED_12345")
                .status(TriQRStatus.PAYED)
                .build();

        TripQRDTO.tripQRResponse response = tripQRMapper.toResponse(tripQR);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("QR_SEED_12345", response.qrSeed());
        assertEquals(TriQRStatus.PAYED, response.status());
    }

    @Test
    void testToResponseWithConfirmedStatus() {
        TripQR tripQR = TripQR.builder()
                .id(2L)
                .qrSeed("QR_SEED_67890")
                .status(TriQRStatus.CONFIRMED)
                .build();

        TripQRDTO.tripQRResponse response = tripQRMapper.toResponse(tripQR);

        assertNotNull(response);
        assertEquals(2L, response.id());
        assertEquals("QR_SEED_67890", response.qrSeed());
        assertEquals(TriQRStatus.CONFIRMED, response.status());
    }

    @Test
    void testToResponseWithCancelledStatus() {
        TripQR tripQR = TripQR.builder()
                .id(3L)
                .qrSeed("QR_SEED_11111")
                .status(TriQRStatus.CANCELLED)
                .build();

        TripQRDTO.tripQRResponse response = tripQRMapper.toResponse(tripQR);

        assertNotNull(response);
        assertEquals(3L, response.id());
        assertEquals("QR_SEED_11111", response.qrSeed());
        assertEquals(TriQRStatus.CANCELLED, response.status());
    }

    @Test
    void testToResponseWithNullValues() {
        TripQR tripQR = TripQR.builder()
                .id(null)
                .qrSeed(null)
                .status(null)
                .build();

        TripQRDTO.tripQRResponse response = tripQRMapper.toResponse(tripQR);

        assertNotNull(response);
        assertNull(response.id());
        assertNull(response.qrSeed());
        assertNull(response.status());
    }

    @Test
    void testToResponseWithEmptyQrSeed() {
        TripQR tripQR = TripQR.builder()
                .id(4L)
                .qrSeed("")
                .status(TriQRStatus.CONFIRMED)
                .build();

        TripQRDTO.tripQRResponse response = tripQRMapper.toResponse(tripQR);

        assertNotNull(response);
        assertEquals(4L, response.id());
        assertEquals("", response.qrSeed());
        assertEquals(TriQRStatus.CONFIRMED, response.status());
    }

    @Test
    void testToResponseWithLongQrSeed() {
        String longQrSeed = "QR_" + "A".repeat(100);
        TripQR tripQR = TripQR.builder()
                .id(5L)
                .qrSeed(longQrSeed)
                .status(TriQRStatus.CONFIRMED)
                .build();

        TripQRDTO.tripQRResponse response = tripQRMapper.toResponse(tripQR);

        assertNotNull(response);
        assertEquals(5L, response.id());
        assertEquals(longQrSeed, response.qrSeed());
        assertEquals(TriQRStatus.CONFIRMED, response.status());
    }
}