package com.example.transmagdalena.Security.Domine.AppUser.Controller;

import com.example.transmagdalena.Security.Config.jwt.JwtService;
import com.example.transmagdalena.Security.Domine.AppUser.AppUser;
import com.example.transmagdalena.Security.Domine.AppUser.DTO.AuthDTO;
import com.example.transmagdalena.Security.Domine.AppUser.Repo.AppUserRepository;
import com.example.transmagdalena.Security.Domine.AppUser.SecurityRols;
import com.example.transmagdalena.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;
    private final UserRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<AuthDTO.AuthResponse> register(@Valid @RequestBody AuthDTO.RegisterRequest req) {
        if (users.existsByEmailIgnoreCase(req.email())) {
            return ResponseEntity.badRequest().build();
        }


        var user = AppUser.builder()
                .email(req.email())
                .password(encoder.encode(req.password()))
                .roles(Set.of(SecurityRols.ROLE_USER))
                .build();

        users.save(user);

        var workUser = com.example.transmagdalena.user.User.builder()
                .phone(req.phone())
                .name(req.name())
                .rol(req.rol())
                .email(req.email())
                .createdAt(LocalDateTime.now())
                .bornDate(req.bornDate())
                .build();

        userRepo.save(workUser);

        var principal = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .toArray(GrantedAuthority[]::new))
                .build();

        var token = jwt.generateToken(principal, Map.of("roles", user.getRoles())); // ← Todos los roles
        return ResponseEntity.ok(new AuthDTO.AuthResponse(token, "Bearer", jwt.getExpirationSeconds()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO.AuthResponse> login(@Valid @RequestBody AuthDTO.LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        var user = users.findByEmailIgnoreCase(req.email()).orElseThrow();

        var principal = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream() // ← CORREGIDO
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .toArray(GrantedAuthority[]::new))
                .build();

        var token = jwt.generateToken(principal, Map.of("roles", user.getRoles()));
        return ResponseEntity.ok(new AuthDTO.AuthResponse(token, "Bearer", jwt.getExpirationSeconds()));
    }


}
