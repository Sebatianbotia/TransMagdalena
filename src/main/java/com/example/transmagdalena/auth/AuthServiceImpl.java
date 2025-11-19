package com.example.transmagdalena.auth;

import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.Mapper.UserMapper;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.UserRols;
import com.example.transmagdalena.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public AuthDTO.longinResponse getPassword(AuthDTO.loginRequest loginRequest) {
        List<User> users = userRepository.findByEmail(loginRequest.email());
        if (users.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        User user = users.getFirst();
        if (user.getRol() == UserRols.ADMIN) {
            boolean check = Objects.equals(user.getPasswordHash(), loginRequest.password());
            if (check) {
                AuthDTO.longinResponse authDTO = new AuthDTO.longinResponse(true,"TokenProvisional", userMapper.toResponse(user));
                return authDTO;
            }
            else{
                throw  new RuntimeException("Credenciales Invalidas");
            }
        }
        throw new RuntimeException("NO AUTORIZADO");
    }
}
