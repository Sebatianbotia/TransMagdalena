package com.example.transmagdalena.bus.mapper;


import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.bus.DTO.BusDTO;
import com.example.transmagdalena.bus.DTO.BusDTO.*;
import com.example.transmagdalena.seat.Seat;
import org.mapstruct.*;

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
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(busCreateRequest updateRequest, @MappingTarget Bus bus);

    @Mapping(target = "seats", source = "seats")
    busResponse toBusDTO(Bus entity);

    seatResponseDto toSeatDTO(Seat entity);
    //así debería funcionar, pruebalo por favor
    // java.util.ConcurrentModificationException da ese error
    Set<seatResponseDto> toSeatsDTO(Set<Seat> entity);


}
