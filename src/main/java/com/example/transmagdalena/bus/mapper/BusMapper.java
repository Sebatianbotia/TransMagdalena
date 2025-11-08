package com.example.transmagdalena.bus.mapper;


import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.bus.DTO.BusDTO;
import com.example.transmagdalena.bus.DTO.BusDTO.*;
import com.example.transmagdalena.seat.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BusMapper {
    //Toentuty
    @Mapping(target="id", ignore = true)
    @Mapping(target = "trips", ignore = true)
    @Mapping(target = "seats", ignore = true)
    Bus toEntity(busCreateRequest createRequest);

    @Mapping(target = "trips", ignore = true)
    @Mapping(target = "seats", ignore = true)
    void updateEntity(busCreateRequest updateRequest, @MappingTarget Bus bus);

    busResponse toBusDTO(Bus entity);

    seatResponseDto toSeatDTO(Seat entity);

    default Set<seatResponseDto> mapSeats(Set<Seat> seats) {
        if (seats == null) {
            return null;
        }
        return seats.stream()
                .map(this::toSeatDTO)
                .collect(Collectors.toSet());
    }

}
