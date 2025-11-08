package com.example.transmagdalena.parcel.Mapper;

import com.example.transmagdalena.parcel.DTO.ParcelDTO;
import com.example.transmagdalena.parcel.Parcel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ParcelMapper {

    Parcel toEntity(ParcelDTO.parcelCreteRequest dto);

    void update(ParcelDTO.parcelUpdateRequest dto, @MappingTarget Parcel parcel);

    ParcelDTO.parcelResponse toDto(Parcel parcel);
}
