package com.example.transmagdalena.baggage.mapper;


import com.example.transmagdalena.assignment.Assignment;
import com.example.transmagdalena.baggage.Baggage;
import com.example.transmagdalena.baggage.DTO.BaggageDTO.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BaggageMapper {
    //create
    @Mapping(target = "id", ignore = true)
    Baggage toEntity(baggageCreateRequest createRequest);
    //update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(baggageUpdateRequest updateRequest, @MappingTarget Baggage baggage);

    baggageResponse toDto(Baggage baggage);


}
