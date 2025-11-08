package com.example.transmagdalena.parcel.Mapper;

import com.example.transmagdalena.parcel.DTO.ParcelDTO;
import com.example.transmagdalena.parcel.Parcel;
import com.example.transmagdalena.parcel.ParcelStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ParcelMapperTest {
    private final ParcelMapper parcelMapper = Mappers.getMapper(ParcelMapper.class);

    @Test
    void testToEntity() {
        ParcelDTO.parcelCreteRequest createRequest = new ParcelDTO.parcelCreteRequest("PKG001", "Juan Perez", "3001234567", "Maria Lopez", "3007654321", new BigDecimal("50000.00"), ParcelStatus.DELIVERED, "http://photo.url", 1234);

        Parcel parcel = parcelMapper.toEntity(createRequest);

        assertNotNull(parcel);
        assertEquals("PKG001", parcel.getCode());
        assertEquals("Juan Perez", parcel.getSenderName());
        assertEquals("3001234567", parcel.getSenderPhone());
        assertEquals("http://photo.url", parcel.getProofPhotoUrl());

    }

    @Test
    void testUpdate() {
        Parcel existingParcel = Parcel.builder().id(1L).code("OLD001").senderName("Old Sender").senderPhone("3000000000").receiverName("Old Receiver").receiverPhone("3009999999").price(new BigDecimal("10000.00")).status(ParcelStatus.CREATED).proofPhotoUrl(null).deliveryOtp(null).build();
        ParcelDTO.parcelUpdateRequest updateRequest = new ParcelDTO.parcelUpdateRequest("NEW001", "New Sender", "3001111111", "New Receiver", "3008888888", new BigDecimal("75000.00"), ParcelStatus.DELIVERED, "http://new.photo", 5678);

        parcelMapper.update(updateRequest, existingParcel);

        assertEquals("NEW001", existingParcel.getCode());
        assertEquals("New Sender", existingParcel.getSenderName());
        assertEquals("http://new.photo", existingParcel.getProofPhotoUrl());
        assertEquals(5678, existingParcel.getDeliveryOtp());
    }

    @Test
    void testToDto() {
        Parcel parcel = Parcel.builder().id(1L).code("PKG999").senderName("Carlos Ruiz").senderPhone("3002222222").receiverName("Ana Garcia").receiverPhone("3007777777").price(new BigDecimal("120000.00")).status(ParcelStatus.IN_TRANSIT).proofPhotoUrl("http://proof.url").deliveryOtp(9999).build();

        ParcelDTO.parcelResponse response = parcelMapper.toDto(parcel);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("PKG999", response.code());
        assertEquals(ParcelStatus.IN_TRANSIT, response.status());
        assertEquals("http://proof.url", response.proofPhotoUrl());
    }

}