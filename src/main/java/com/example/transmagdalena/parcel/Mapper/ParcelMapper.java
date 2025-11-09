package com.example.transmagdalena.parcel.Mapper;

import com.example.transmagdalena.parcel.DTO.ParcelDTO;
import com.example.transmagdalena.parcel.Parcel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ParcelMapper {

    Parcel toEntity(ParcelDTO.parcelCreateRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(ParcelDTO.parcelUpdateRequest dto, @MappingTarget Parcel parcel);

    ParcelDTO.parcelResponse toDto(Parcel parcel);
}
