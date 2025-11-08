package com.example.transmagdalena.user.Mapper;

import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    public final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToEntity() {
        UserDTO.userCreateRequest createRequest = new UserDTO.userCreateRequest(
                "Ana Pérez",
                "ana.perez@example.com",
                "3100000000",
                UserRols.PASSENGER,
                "hashedpwd123"
        );

        User user = userMapper.toEntity(createRequest);

        assertNotNull(user);
        assertNull(user.getId());
        assertEquals("Ana Pérez", user.getName());
        assertEquals("ana.perez@example.com", user.getEmail());
        assertEquals("3100000000", user.getPhone());
        assertEquals(UserRols.PASSENGER, user.getRol());
        assertEquals("hashedpwd123", user.getPasswordHash());
        assertNull(user.getCreatedAt());
    }

    @Test
    void testUpdate() {
        User existing = User.builder()
                .id(42L)
                .name("Old Name")
                .email("old@example.com")
                .phone("3001112222")
                .rol(UserRols.CLERK)
                .passwordHash("oldhash")
                .build();

        UserDTO.userUpdateRequest updateRequest = new UserDTO.userUpdateRequest(
                "New Name",
                "new@example.com",
                "3111112222",
                UserRols.ADMIN,
                "newhash"
        );

        userMapper.update(updateRequest, existing);

        assertEquals(42L, existing.getId());
        assertEquals("New Name", existing.getName());
        assertEquals("new@example.com", existing.getEmail());
        assertEquals("3111112222", existing.getPhone());
        assertEquals(UserRols.ADMIN, existing.getRol());
        assertEquals("newhash", existing.getPasswordHash());
    }

    @Test
    void testToResponse() {
        User user = User.builder()
                .id(7L)
                .name("Luis Gómez")
                .email("luis@example.com")
                .phone("3200000000")
                .rol(UserRols.DRIVER)
                .build();

        UserDTO.userResponse resp = userMapper.toResponse(user);

        assertNotNull(resp);
        assertEquals(7L, resp.id());
        assertEquals("Luis Gómez", resp.name());
        assertEquals("luis@example.com", resp.email());
        assertEquals("3200000000", resp.phone());
        assertEquals(UserRols.DRIVER, resp.rol());
    }


}