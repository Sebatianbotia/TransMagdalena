package com.example.transmagdalena.parcel.repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.parcel.Parcel;
import com.example.transmagdalena.parcel.ParcelStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

public class ParcelRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private ParcelRepository parcelRepository;

    @Test
    @DisplayName("Buscar por id")
    public void buscarPorId(){

        Parcel parcel = Parcel.builder().price(BigDecimal.valueOf(222)).deliveryOtp(222)
                .receiverName("Jose").receiverPhone("sss").code("ss").senderPhone("222")
                .senderName("Luis").status(ParcelStatus.CREATED).build();
        parcel = parcelRepository.save(parcel);

        Optional<Parcel> optional = parcelRepository.findById(parcel.getId());
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(optional.get().getPrice(), parcel.getPrice());
        Assertions.assertEquals(optional.get().getId(), parcel.getId());
        Assertions.assertEquals(optional.get(), parcel);

    }

    @Test
    @DisplayName("Buscar por codigo")
    public void buscarPorCodigo(){


        Parcel parcel = Parcel.builder().price(BigDecimal.valueOf(222)).deliveryOtp(222)
                .receiverName("Jose").receiverPhone("sss").code("ss").senderPhone("222")
                .senderName("Luis").status(ParcelStatus.CREATED).build();
        parcel = parcelRepository.save(parcel);

        Optional<Parcel> optional = parcelRepository.findByCode(parcel.getCode());
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(optional.get().getPrice(), parcel.getPrice());
        Assertions.assertEquals(optional.get().getId(), parcel.getId());
        Assertions.assertEquals(optional.get(), parcel);

    }
}
