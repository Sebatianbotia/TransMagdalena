package com.example.transmagdalena.user.repository;

import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Page<User> findAll(Pageable pageable);

    Optional<User> findUserByIdAndRolIs(Long id, UserRols rol);

    Page<User> findUserByRolIs(UserRols rol, Pageable pageable);
    Integer countUsersByRolIs(UserRols rol);

    List<User> findByEmail(String email);
}
