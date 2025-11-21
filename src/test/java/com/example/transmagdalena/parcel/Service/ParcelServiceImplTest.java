package com.example.transmagdalena.parcel.Service;

import com.example.transmagdalena.parcel.DTO.ParcelDTO;
import com.example.transmagdalena.parcel.Mapper.ParcelMapper;
import com.example.transmagdalena.parcel.Parcel;
import com.example.transmagdalena.parcel.ParcelStatus;
import com.example.transmagdalena.parcel.repository.ParcelRepository;
import com.example.transmagdalena.parcel.service.ParcelServiceImpl;
import com.example.transmagdalena.utilities.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParcelServiceImplTest {

    @Mock
    ParcelRepository parcelRepository;

    @Mock
    ParcelMapper parcelMapper;

    @InjectMocks
    ParcelServiceImpl service;

    @Test
    void shouldCreateParcel() {
        var req = new ParcelDTO.parcelCreateRequest(
                "PKG001",
                "Alice",
                "300123",
                "Bob",
                "310456",
                new BigDecimal("15000"),
                ParcelStatus.CREATED,
                "photo.jpg",
                1111
        );

        var entity = Parcel.builder()
                .code("PKG001")
                .senderName("Alice")
                .senderPhone("300123")
                .receiverName("Bob")
                .receiverPhone("310456")
                .price(new BigDecimal("15000"))
                .status(ParcelStatus.CREATED)
                .proofPhotoUrl("photo.jpg")
                .deliveryOtp(1111)
                .build();

        when(parcelMapper.toEntity(req)).thenReturn(entity);

        when(parcelRepository.save(entity)).thenAnswer(inv -> {
            Parcel p = inv.getArgument(0);
            p.setId(50L);
            return p;
        });

        var dto = new ParcelDTO.parcelResponse(
                50L, "PKG001", "Alice", "300123", "Bob", "310456",
                new BigDecimal("15000"), ParcelStatus.CREATED, "photo.jpg", 1111
        );

        when(parcelMapper.toDto(any())).thenReturn(dto);

        var result = service.save(req);

        assertThat(result.id()).isEqualTo(50L);
        assertThat(result.code()).isEqualTo("PKG001");
        verify(parcelRepository).save(entity);
    }

    @Test
    void shouldReturnParcelById() {
        var entity = Parcel.builder()
                .id(7L)
                .code("PKG007")
                .status(ParcelStatus.CREATED)
                .build();

        when(parcelRepository.findById(7L)).thenReturn(Optional.of(entity));

        var dto = new ParcelDTO.parcelResponse(
                7L, "PKG007", null, null, null, null, null, ParcelStatus.CREATED, null, null
        );

        when(parcelMapper.toDto(entity)).thenReturn(dto);

        var result = service.get(7L);

        assertThat(result.id()).isEqualTo(7L);
        assertThat(result.status()).isEqualTo(ParcelStatus.CREATED);
    }

    @Test
    void shouldThrowOnGetByIdWhenNotFound() {
        when(parcelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getObject(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Parcel not found");
    }

    @Test
    void shouldReturnParcelByCode() {
        var entity = Parcel.builder()
                .id(3L)
                .code("AA123")
                .build();

        when(parcelRepository.findByCode("AA123")).thenReturn(Optional.of(entity));

        var dto = new ParcelDTO.parcelResponse(
                3L, "AA123", null, null, null, null, null, null, null, null
        );

        when(parcelMapper.toDto(entity)).thenReturn(dto);

        var result = service.get("AA123");

        assertThat(result.code()).isEqualTo("AA123");
    }

    @Test
    void shouldThrowOnGetByCodeWhenNotFound() {
        when(parcelRepository.findByCode("NOPE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getObject("NOPE"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Parcel not found");
    }

    @Test
    void shouldReturnAllParcelsPaged() {
        var entity = Parcel.builder().id(1L).code("X").build();
        var page = new PageImpl<>(java.util.List.of(entity));

        when(parcelRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        when(parcelMapper.toDto(entity)).thenReturn(
                new ParcelDTO.parcelResponse(1L, "X", null, null, null, null, null, null, null, null)
        );

        var result = service.getAll(PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).code()).isEqualTo("X");
    }

    @Test
    void shouldUpdateParcel() {
        var existing = Parcel.builder()
                .id(5L)
                .code("OLD")
                .senderName("John")
                .build();

        when(parcelRepository.findById(5L)).thenReturn(Optional.of(existing));

        var req = new ParcelDTO.parcelUpdateRequest(
                "NEW",
                "Ana",
                null,
                null,
                null,
                new BigDecimal("20000"),
                ParcelStatus.IN_TRANSIT,
                "new_photo.jpg",
                2222
        );

        // simulate mapper.update()
        doAnswer(inv -> {
            var r = (ParcelDTO.parcelUpdateRequest) inv.getArgument(0);
            var p = (Parcel) inv.getArgument(1);
            p.setCode(r.code());
            p.setSenderName(r.senderName());
            p.setPrice(r.price());
            p.setStatus(r.status());
            p.setProofPhotoUrl(r.proofPhotoUrl());
            p.setDeliveryOtp(r.deliveryOtp());
            return null;
        }).when(parcelMapper).update(req, existing);

        var expectedDto = new ParcelDTO.parcelResponse(
                5L, "NEW", "Ana", null, null, null, new BigDecimal("20000"),
                ParcelStatus.IN_TRANSIT, "new_photo.jpg", 2222
        );

        when(parcelMapper.toDto(existing)).thenReturn(expectedDto);

        var result = service.update(req, 5L);

        assertThat(result.code()).isEqualTo("NEW");
        assertThat(result.price()).isEqualTo(new BigDecimal("20000"));
        verify(parcelMapper).update(req, existing);
    }

    @Test
    void shouldDeleteParcel() {
        var entity = Parcel.builder()
                .id(9L)
                .code("DEL9")
                .build();

        when(parcelRepository.findById(9L)).thenReturn(Optional.of(entity));

        boolean deleted = service.delete(9L);

        assertThat(deleted).isTrue();
        verify(parcelRepository).deleteById(9L);
    }
}
