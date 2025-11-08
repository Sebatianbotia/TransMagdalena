package com.example.transmagdalena.city.DTO;
import java.io.Serializable;

public class CityDTO {
    public record cityCreateRequest(String name, float lat, float lon) implements  Serializable{}
    public record cityUpdateRequest(String name, float lat, float lon) implements Serializable {}
    public record cityResponse(Long id ,String name, float lat, float lon) implements  Serializable{}
}
