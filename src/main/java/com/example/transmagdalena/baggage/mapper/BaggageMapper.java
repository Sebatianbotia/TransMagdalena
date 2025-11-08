package com.example.transmagdalena.baggage.mapper;


import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.baggage.Baggage;
import com.example.transmagdalena.baggage.DTO.BaggageDTO.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BaggageMapper {
    //create
    @Mapping(target = "id", ignore = true)
    Baggage toEntity(baggageCreateRequest createRequest);
    //update
    void updateEntity(baggageUpdateRequest updateRequest, Baggage baggage);

    baggageResponse toDto(Baggage baggage);


}
