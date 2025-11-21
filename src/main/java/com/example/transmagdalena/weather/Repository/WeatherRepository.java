package com.example.transmagdalena.weather.Repository;

import com.example.transmagdalena.weather.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;


public interface WeatherRepository extends JpaRepository<Weather,Long> {

    @Query("""
    select w from Weather w
    where w.date = :date and w.startTime <= :targetTime and w.endTime >= :targetTime and w.city.id = :city
    order by w.startTime asc
    limit 1
""")
    public Optional<Weather> getWeatherByDateAndTime(@Param("date") LocalDate date,
                                                     @Param("targetTime") LocalTime targetTime,
                                                     @Param("city") Long city);

}
