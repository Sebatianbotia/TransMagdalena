package com.example.transmagdalena.config.Service;

import com.example.transmagdalena.config.Config;
import com.example.transmagdalena.config.ConfigType;
import com.example.transmagdalena.config.DTO.ConfigDTO;
import com.example.transmagdalena.user.UserRols;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConfigService {

    ConfigDTO.configResponse save(ConfigDTO.configCreateRequest request);
    ConfigDTO.configResponse update(Long id, ConfigDTO.configUpdateRequest request);
    ConfigDTO.configResponse get(Long id);
    ConfigDTO.configResponse get(UserRols type);
    Page<ConfigDTO.configResponse> getAll(Pageable pageable);
    void delete(Long id);
    Config getObject(Long id);


}
