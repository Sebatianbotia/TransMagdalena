package com.example.transmagdalena.fareRule.Mapper;

import com.example.transmagdalena.fareRule.DTO.FareRuleDTO;
import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.route.Route;
import com.example.transmagdalena.routeStop.DTO.RouteStopDTO;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.stop.Stop;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FareRuleMapper {

    FareRule toEntity(FareRuleDTO.fareRuleCreateRequest fareRuleDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(FareRuleDTO.fareRuleUpdateRequest fareRuleUpdateRequest,  @MappingTarget FareRule fareRule);

    FareRuleDTO.fareRuleResponse toDto(FareRule fareRule);
    FareRuleDTO.stopDTO toStopDTO(Stop entity);
}
