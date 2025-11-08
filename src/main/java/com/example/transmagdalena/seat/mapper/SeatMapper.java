package com.example.transmagdalena.seat.mapper;

import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.seat.DTO.SeatDTO;
import com.example.transmagdalena.seat.DTO.SeatDTO.*;
import com.example.transmagdalena.seat.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bus", ignore = true)
    Seat toEntity(seatCreateRequest seatCreateRequest);

    @Mapping(target = "bus", ignore = true)
    void updateSeat(@MappingTarget Seat seat, SeatDTO.seatUpdateRequest seatDTO);

    seatResponse toSeatResponse(Seat seat);

    busDto toBusDTO(Bus bus);

}
