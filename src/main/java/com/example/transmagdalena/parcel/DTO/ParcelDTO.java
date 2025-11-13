package com.example.transmagdalena.parcel.DTO;

import com.example.transmagdalena.parcel.ParcelStatus;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

public class ParcelDTO {

    public record parcelCreateRequest(@NotNull String code, @NotNull String senderName,
                                     @NotNull String senderPhone, @NotNull String receiverName,
                                     @NotNull String receiverPhone, @NotNull BigDecimal price,
                                     @NotNull ParcelStatus status, String proofPhotoUrl,
                                     Integer deliveryOtp
                                     ) implements Serializable {}

    public record parcelUpdateRequest(String code, String senderName,
                                       String senderPhone,  String receiverName,
                                       String receiverPhone,  BigDecimal price,
                                       ParcelStatus status, String proofPhotoUrl,
                                      Integer deliveryOtp) implements Serializable {}

    public record parcelResponse(Long id, String code,  String senderName,
                                  String senderPhone,  String receiverName,
                                  String receiverPhone,  BigDecimal price,
                                  ParcelStatus status, String proofPhotoUrl,
                                 Integer deliveryOtp) implements Serializable {}
}
