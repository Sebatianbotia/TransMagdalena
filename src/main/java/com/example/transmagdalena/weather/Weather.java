package com.example.transmagdalena.weather;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.configuration.Configuration;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "weather_problems")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private WeatherType weatherType;

    private Float discount;

    @JsonFormat(pattern = Configuration.DATE_FORMAT)
    private LocalDate date;

    @JsonFormat(pattern = Configuration.HOUR_FORMAT)
    private LocalTime startTime;

    @JsonFormat(pattern = Configuration.HOUR_FORMAT)
    private LocalTime endTime;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    public void setCity(City city) {
        if (city == this.city){
            return;
        }
        var oldCity = this.city;
        if (oldCity != null) {
            oldCity.getWeathers().remove(this);
        }
        this.city = city;
        this.city.getWeathers().add(this);
    }


}
