package com.example.transmagdalena.user.Service;

import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.Mapper.UserMapper;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO.userResponse save(UserDTO.userCreateRequest userCreateRequest) {
        var entity = userMapper.toEntity(userCreateRequest);
        return userMapper.toResponse(userRepository.save(entity));
    }

    @Override
    public UserDTO.userResponse get(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserDTO.userResponse> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toResponse).toList();
    }
}
