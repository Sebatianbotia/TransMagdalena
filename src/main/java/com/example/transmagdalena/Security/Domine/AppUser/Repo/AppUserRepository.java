package com.example.transmagdalena.Security.Domine.AppUser.Repo;

import com.example.transmagdalena.Security.Domine.AppUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);

}
