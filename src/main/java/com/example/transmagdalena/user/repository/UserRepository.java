package com.example.transmagdalena.user.repository;

import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User,Long> {

    Page<User> findAll(Pageable pageable);

    Optional<User> findUserByIdAndRolIs(Long id, UserRols rol);

    Page<User> findUserByRolIs(UserRols rol, Pageable pageable);
    Integer countUsersByRolIs(UserRols rol);

    @Query("""
    select u from User u
    where u.rol in :rols
""")
    Page<User> findPassengers(Pageable pageable, @Param("rols") Set<UserRols> rols);
}
