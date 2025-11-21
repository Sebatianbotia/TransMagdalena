package com.example.transmagdalena.user.Service;

import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.Mapper.UserMapper;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import com.example.transmagdalena.user.repository.UserRepository;
import com.example.transmagdalena.utilities.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock UserMapper userMapper;

    @InjectMocks UserServiceImpl service;

    private User user() {
        return User.builder()
                .id(10L)
                .name("John")
                .email("john@test.com")
                .phone("111")
                .rol(UserRols.PASSENGER)
                .bornDate(LocalDate.of(2000,1,1))
                .createdAt(LocalDateTime.now())
                .driverAssignments(new HashSet<>())
                .dispatcherAssignments(new HashSet<>())
                .build();
    }

    // -----------------------------------------------------
    @Test
    void shouldSaveUser() {
        var req = new UserDTO.userCreateRequest(
                "John","john@test.com","111",UserRols.PASSENGER, LocalDate.of(2000,1,1)
        );

        User entity = user();
        entity.setId(null); // antes de guardar
        User saved = user(); // lo que retorna save()

        when(userMapper.toEntity(req)).thenReturn(entity);
        when(userRepository.save(any())).thenReturn(saved);
        when(userMapper.toResponse(saved))
                .thenReturn(new UserDTO.userResponse(
                        10L,"John","john@test.com","111",
                        UserRols.PASSENGER, LocalDate.of(2000,1,1)
                ));

        var result = service.save(req);

        assertThat(result.id()).isEqualTo(10L);
        verify(userRepository).save(entity);
    }

    // -----------------------------------------------------
    @Test
    void shouldGetUser() {
        var u = user();

        when(userRepository.findById(10L)).thenReturn(Optional.of(u));
        when(userMapper.toResponse(u))
                .thenReturn(new UserDTO.userResponse(
                        10L,"John","john@test.com","111",
                        UserRols.PASSENGER, LocalDate.of(2000,1,1)
                ));

        var result = service.get(10L);

        assertThat(result.id()).isEqualTo(10L);
    }

    // -----------------------------------------------------
    @Test
    void shouldReturnAssignments() {
        var u = user();

        when(userRepository.findById(10L)).thenReturn(Optional.of(u));

        when(userMapper.toResponseAssigment(u))
                .thenReturn(new UserDTO.userAssigmentResponse(
                        10L,"John","john@test.com","111",
                        UserRols.PASSENGER,
                        Set.of(), Set.of()
                ));

        var result = service.getAssigments(10L);

        assertThat(result.id()).isEqualTo(10L);
    }

    // -----------------------------------------------------
    @Test
    void shouldGetAllUsers() {
        var u = user();
        Pageable pageable = PageRequest.of(0,5);

        when(userRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(u)));

        when(userMapper.toResponse(u))
                .thenReturn(new UserDTO.userResponse(
                        10L,"John","john@test.com","111",
                        UserRols.PASSENGER,LocalDate.of(2000,1,1)
                ));

        var result = service.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(10L);
    }

    // -----------------------------------------------------
    @Test
    void shouldDeleteUser() {
        var u = user();

        when(userRepository.findById(10L)).thenReturn(Optional.of(u));

        boolean deleted = service.delete(10L);

        assertThat(deleted).isTrue();
        verify(userRepository).deleteById(10L);
    }

    // -----------------------------------------------------
    @Test
    void shouldUpdateUser() {
        var u = user();

        var req = new UserDTO.userUpdateRequest("A","B","C",UserRols.DRIVER);

        // simulate mapper.update
        doAnswer(inv -> {
            UserDTO.userUpdateRequest r = inv.getArgument(0);
            User usr = inv.getArgument(1);
            usr.setName(r.name());
            usr.setEmail(r.email());
            usr.setPhone(r.phone());
            usr.setRol(r.rol());
            return null;
        }).when(userMapper).update(any(), any());

        when(userRepository.findById(10L)).thenReturn(Optional.of(u));

        when(userMapper.toResponse(u))
                .thenReturn(new UserDTO.userResponse(
                        10L,"A","B","C",UserRols.DRIVER,
                        LocalDate.of(2000,1,1)
                ));

        var result = service.update(req,10L);

        assertThat(result.name()).isEqualTo("A");
        assertThat(result.rol()).isEqualTo(UserRols.DRIVER);
    }

    // -----------------------------------------------------
    @Test
    void shouldGetUsersByRol() {
        var u = user();
        Pageable pageable = PageRequest.of(0,5);

        when(userRepository.findUserByRolIs(UserRols.PASSENGER, pageable))
                .thenReturn(new PageImpl<>(List.of(u)));

        when(userMapper.toResponse(u))
                .thenReturn(new UserDTO.userResponse(
                        10L,"John","john@test.com","111",
                        UserRols.PASSENGER,LocalDate.of(2000,1,1)
                ));

        var res = service.getUsersByRol(UserRols.PASSENGER, pageable);

        assertThat(res.getContent()).hasSize(1);
    }

    // -----------------------------------------------------
    @Test
    void shouldGetPassengers() {
        var u = user();
        Pageable pageable = PageRequest.of(0,5);

        when(userRepository.findPassengers(pageable,
                Set.of(UserRols.PASSENGER, UserRols.STUDENT, UserRols.OLD_MAN)))
                .thenReturn(new PageImpl<>(List.of(u)));

        when(userMapper.toResponse(u))
                .thenReturn(new UserDTO.userResponse(
                        10L,"John","john@test.com","111",
                        UserRols.PASSENGER,LocalDate.of(2000,1,1)
                ));

        var res = service.getPassengers(pageable);

        assertThat(res.getContent()).hasSize(1);
    }

    // -----------------------------------------------------
    @Test
    void shouldCountUsersByRol() {
        when(userRepository.countUsersByRolIs(UserRols.PASSENGER)).thenReturn(8);

        var result = service.countUsersByRol(UserRols.PASSENGER);

        assertThat(result).isEqualTo(8);
    }

    // -----------------------------------------------------
    @Test
    void shouldGetObjectByRolSuccessfully() {
        var u = user();

        when(userRepository.findById(10L)).thenReturn(Optional.of(u));

        var result = service.getObject(10L, UserRols.PASSENGER);

        assertThat(result.getRol()).isEqualTo(UserRols.PASSENGER);
    }

    // -----------------------------------------------------
    @Test
    void shouldThrowWhenRolDoesNotMatch() {
        var u = user();

        when(userRepository.findById(10L)).thenReturn(Optional.of(u));

        assertThatThrownBy(() ->
                service.getObject(10L, UserRols.DRIVER)
        ).isInstanceOf(NotFoundException.class);
    }
}
