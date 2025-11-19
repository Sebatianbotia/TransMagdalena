package com.example.transmagdalena.config.DTO;

import com.example.transmagdalena.config.ConfigType;

import java.io.Serializable;

public class ConfigDTO {

    public record configCreateRequest(ConfigType type, Float value) implements Serializable{}
    public record configUpdateRequest(ConfigType type, Float value) implements Serializable{}
    public record configResponse(Long id, ConfigType type, Float value) implements Serializable{}


}
