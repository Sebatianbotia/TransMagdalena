package com.example.transmagdalena.user.Repository;

import com.example.transmagdalena.AbstractRepositoryPSQL;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import com.example.transmagdalena.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserRepositoryTest extends AbstractRepositoryPSQL {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("buscar todos los usuarios con paginación")
    void buscarTodosLosUsuariosConPaginacion() {

        User user1 = User.builder()
                .name("Juan Perez")
                .email("juan@example.com")
                .phone("3001112222")
                .rol(UserRols.CLERK)
                .createdAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .name("Maria Lopez")
                .email("maria@example.com")
                .phone("3103334444")
                .rol(UserRols.DRIVER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = userRepository.findAll(pageable);

        Assertions.assertEquals(2, page.getTotalElements());
        Assertions.assertTrue(page.getContent().stream().anyMatch(u -> "Juan Perez".equals(u.getName())));
        Assertions.assertTrue(page.getContent().stream().anyMatch(u -> "Maria Lopez".equals(u.getName())));
    }

    @Test
    @DisplayName("buscar usuario por id y rol")
    void buscarUsuarioPorIdYRol() {

        User user = User.builder()
                .name("Carlos Ruiz")
                .email("carlos@example.com")
                .phone("3205556666")
                .rol(UserRols.DISPATCHER)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        Optional<User> optional = userRepository.findUserByIdAndRolIs(user.getId(), UserRols.DISPATCHER);

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(user.getId(), optional.get().getId());
        Assertions.assertEquals(UserRols.DISPATCHER, optional.get().getRol());
        Assertions.assertEquals("Carlos Ruiz", optional.get().getName());
    }

    @Test
    @DisplayName("buscar usuario por id y rol no coincidente")
    void buscarUsuarioPorIdYRolNoCoincidente() {

        User user = User.builder()
                .name("Ana Torres")
                .email("ana@example.com")
                .phone("3157778888")
                .rol(UserRols.CLERK)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        Optional<User> optional = userRepository.findUserByIdAndRolIs(user.getId(), UserRols.DRIVER);

        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    @DisplayName("buscar usuarios por rol")
    void buscarUsuariosPorRol() {

        User user1 = User.builder()
                .name("Pedro Martinez")
                .email("pedro@example.com")
                .phone("3179990000")
                .rol(UserRols.DRIVER)
                .createdAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .name("Luisa Garcia")
                .email("luisa@example.com")
                .phone("3181112222")
                .rol(UserRols.DRIVER)
                .createdAt(LocalDateTime.now())
                .build();

        User user3 = User.builder()
                .name("Sofia Ramirez")
                .email("sofia@example.com")
                .phone("3193334444")
                .rol(UserRols.CLERK)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = userRepository.findUserByRolIs(UserRols.DRIVER, pageable);

        Assertions.assertEquals(2, page.getTotalElements());
        Assertions.assertTrue(page.getContent().stream().allMatch(u -> UserRols.DRIVER.equals(u.getRol())));
    }

    @Test
    @DisplayName("contar usuarios por rol")
    void contarUsuariosPorRol() {

        User user1 = User.builder()
                .name("Miguel Fernandez")
                .email("miguel@example.com")
                .phone("3115556666")
                .rol(UserRols.DISPATCHER)
                .createdAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .name("Elena Castro")
                .email("elena@example.com")
                .phone("3127778888")
                .rol(UserRols.DISPATCHER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        Integer count = userRepository.countUsersByRolIs(UserRols.DISPATCHER);

        Assertions.assertEquals(2, count);
    }

    @Test
    @DisplayName("contar usuarios por rol sin resultados")
    void contarUsuariosPorRolSinResultados() {

        Integer count = userRepository.countUsersByRolIs(UserRols.ADMIN);

        Assertions.assertEquals(0, count);
    }

    @Test
    @DisplayName("buscar pasajeros por roles")
    void buscarPasajerosPorRoles() {

        Set<UserRols> passengerRoles = new HashSet<>();
        passengerRoles.add(UserRols.CLERK);
        passengerRoles.add(UserRols.STUDENT);

        User user1 = User.builder()
                .name("Roberto Silva")
                .email("roberto@example.com")
                .phone("3139990000")
                .rol(UserRols.CLERK)
                .createdAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .name("Carmen Vargas")
                .email("carmen@example.com")
                .phone("3141112222")
                .rol(UserRols.STUDENT)
                .createdAt(LocalDateTime.now())
                .build();

        User user3 = User.builder()
                .name("Jorge Mendez")
                .email("jorge@example.com")
                .phone("3153334444")
                .rol(UserRols.DRIVER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = userRepository.findPassengers(pageable, passengerRoles);

        Assertions.assertEquals(2, page.getTotalElements());
        Assertions.assertTrue(page.getContent().stream().allMatch(u -> passengerRoles.contains(u.getRol())));
    }

    @Test
    @DisplayName("buscar pasajeros por roles vacío")
    void buscarPasajerosPorRolesVacio() {

        Set<UserRols> emptyRoles = new HashSet<>();

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = userRepository.findPassengers(pageable, emptyRoles);

        Assertions.assertEquals(0, page.getTotalElements());
    }
}