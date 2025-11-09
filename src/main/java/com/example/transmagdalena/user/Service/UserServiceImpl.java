package com.example.transmagdalena.user.Service;

import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.Mapper.UserMapper;
import com.example.transmagdalena.user.User;
import com.example.transmagdalena.user.repository.UserRepository;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
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
        entity.setCreatedAt(OffsetDateTime.now());
        return userMapper.toResponse(userRepository.save(entity));
    }

    @Override
    public UserDTO.userResponse get(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public Page<UserDTO.userResponse> getAll(Pageable pageable) {
        var users =  userRepository.findAll(pageable);
        return users.map(userMapper::toResponse);
    }

    @Override
    public boolean delete(Long id) {
         var f = getObject(id);
         boolean check = false;
         if (f!=null) {
             userRepository.deleteById(id);
             check = true;
         }
         return check;
    }

    @Override
    @Transactional
    public UserDTO.userResponse update(UserDTO.userUpdateRequest userUpdateRequest, Long id) {
        var f =  getObject(id);
        userMapper.update(userUpdateRequest, f);
        return userMapper.toResponse(f);
    }

    @Override
    public User getObject(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));
    }

    public User getUser(Long id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));
    }
}
