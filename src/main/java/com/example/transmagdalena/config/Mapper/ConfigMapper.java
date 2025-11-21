package com.example.transmagdalena.config.Mapper;

import com.example.transmagdalena.config.Config;
import com.example.transmagdalena.config.DTO.ConfigDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ConfigMapper {

    Config toEntity(ConfigDTO.configCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(ConfigDTO.configUpdateRequest request, @MappingTarget Config config);

    ConfigDTO.configResponse toDTO(Config config);
}
