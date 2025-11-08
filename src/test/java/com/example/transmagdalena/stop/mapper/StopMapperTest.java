package com.example.transmagdalena.stop.mapper;

import com.example.transmagdalena.city.City;
import com.example.transmagdalena.stop.DTO.StopDTO;
import com.example.transmagdalena.stop.Stop;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class StopMapperTest {
    public final StopMapper stopMapper = Mappers.getMapper(StopMapper.class);

    @Test
    void testToEntity() {
        StopDTO.stopCreateRequest createRequest = new StopDTO.stopCreateRequest(
                "Central Station",
                5L,
                1.23f,
                4.56f
        );

        Stop stop = stopMapper.toEntity(createRequest);

        assertNotNull(stop);
        assertNull(stop.getId());
        assertEquals("Central Station", stop.getName());
        assertEquals(1.23f, stop.getLat(), 0.0001f);
        assertEquals(4.56f, stop.getLng(), 0.0001f);
        assertNull(stop.getCity());
    }

    @Test
    void testUpdateStop() {
        City oldCity = City.builder()
                .name("OldCity")
                .build();

        Stop existing = Stop.builder()
                .id(10L)
                .name("Old Stop")
                .city(oldCity)
                .lat(0.0f)
                .lng(0.0f)
                .build();

        StopDTO.stopUpdateRequest updateRequest = new StopDTO.stopUpdateRequest(
                10L,
                "New Stop Name",
                7L,
                9.87f,
                6.54f
        );

        stopMapper.updateStop(updateRequest, existing);

        assertEquals(10L, existing.getId());
        assertEquals("New Stop Name", existing.getName());
        assertEquals(9.87f, existing.getLat(), 0.0001f);
        assertEquals(6.54f, existing.getLng(), 0.0001f);
        assertNotNull(existing.getCity());
        assertEquals("OldCity", existing.getCity().getName());
    }

    @Test
    void testToDTO() {
        City city = City.builder()
                .name("Gotham")
                .build();

        Stop stop = Stop.builder()
                .id(3L)
                .name("Downtown")
                .city(city)
                .lat(12.34f)
                .lng(56.78f)
                .build();

        StopDTO.stopResponse response = stopMapper.toDTO(stop);

        assertNotNull(response);
        assertEquals(3L, response.id());
        assertEquals("Downtown", response.name());
        assertNotNull(response.city());
        assertEquals("Gotham", response.city().name());
        assertEquals(12.34f, response.lat(), 0.0001f);
        assertEquals(56.78f, response.lng(), 0.0001f);
    }

}