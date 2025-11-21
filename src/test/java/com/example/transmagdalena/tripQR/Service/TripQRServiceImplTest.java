package com.example.transmagdalena.tripQR.Service;

import com.example.transmagdalena.tripQR.DTO.TripQRDTO;
import com.example.transmagdalena.tripQR.Mapper.TripQRMapper;
import com.example.transmagdalena.tripQR.TriQRStatus;
import com.example.transmagdalena.tripQR.TripQR;
import com.example.transmagdalena.tripQR.repository.TripQrRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TripQRServiceImplTest {

    @Mock
    TripQrRepository tripQrRepository;

    @Mock
    TripQRMapper tripQRMapper;

    @InjectMocks
    TripQRServiceImpl service;

    private TripQR entity() {
        return TripQR.builder()
                .id(10L)
                .qrSeed("ABC123")
                .status(TriQRStatus.PAYED)
                .build();
    }

    @Test
    void shouldDeleteTripQR() {
        TripQR qr = entity();

        // ACT
        service.delete(qr);

        // ASSERT
        verify(tripQrRepository, times(1)).delete(qr);
    }

    @Test
    void shouldGetTripQR() {
        Long tripId = 50L;
        Pageable pageable = PageRequest.of(0, 5);

        TripQR qr = entity();

        Page<TripQR> page = new PageImpl<>(List.of(qr), pageable, 1);

        when(tripQrRepository.getTripQRByTripId(tripId, pageable))
                .thenReturn(page);

        when(tripQRMapper.toResponse(qr))
                .thenReturn(new TripQRDTO.tripQRResponse(
                        10L, "ABC123", TriQRStatus.PAYED
                ));

        // ACT
        var res = service.getTripQR(tripId, pageable);

        // ASSERT
        assertThat(res.getContent()).hasSize(1);
        assertThat(res.getContent().get(0).id()).isEqualTo(10L);
        assertThat(res.getContent().get(0).qrSeed()).isEqualTo("ABC123");
        assertThat(res.getContent().get(0).status()).isEqualTo(TriQRStatus.PAYED);

        verify(tripQrRepository).getTripQRByTripId(tripId, pageable);
        verify(tripQRMapper).toResponse(qr);
    }
}
