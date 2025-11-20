package com.example.transmagdalena.config.Service;

import com.example.transmagdalena.config.Config;
import com.example.transmagdalena.config.ConfigType;
import com.example.transmagdalena.config.DTO.ConfigDTO;
import com.example.transmagdalena.config.Mapper.ConfigMapper;
import com.example.transmagdalena.config.repository.ConfigRepository;
import com.example.transmagdalena.user.UserRols;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository configRepository;
    private final ConfigMapper configMapper;

    @Override
    public ConfigDTO.configResponse save(ConfigDTO.configCreateRequest request) {
        var s = configMapper.toEntity(request);
        s.setIsDelete(false);
        if (configRepository.existsConfigByType(request.type())) {
            configRepository.deleteConfigByType(request.type());
        }
        return configMapper.toDTO(configRepository.save(s));
    }

    @Override
    public ConfigDTO.configResponse update(Long id, ConfigDTO.configUpdateRequest request) {
        var s = getObject(id);
        configMapper.update(request, s);
        if (configRepository.existsConfigByType(request.type()) && !s.getType().equals(request.type())) { // va a cambiar de tipo de configuracion, asi que borre el que ya esta
            configRepository.deleteConfigByType(request.type());
        }
        return configMapper.toDTO(s);
    }

    @Override
    public ConfigDTO.configResponse get(Long id) {
        return configMapper.toDTO(getObject(id));
    }

    @Override
    public Page<ConfigDTO.configResponse> getAll(Pageable pageable) {
        return  configRepository.findAll(pageable).map(configMapper::toDTO);
    }

    @Override
    public void delete(Long id) {
        configRepository.deleteById(id);
    }

    @Override
    public Config getObject(Long id){
        return configRepository.findById(id).orElseThrow(() -> new NotFoundException("config not found"));
    }

    @Override
    public ConfigDTO.configResponse get(UserRols type){
        ConfigType type1;
        if (UserRols.STUDENT.equals(type)) {
            type1 = ConfigType.PASSENGER_DISCOUNT;
        }
        else if (UserRols.OLD_MAN.equals(type)) {
            type1 = ConfigType.AGED_DISCOUNT;
        }
        else {
            return new ConfigDTO.configResponse(null,null,0F);
        }
        return configMapper.toDTO(configRepository.findConfigByType(type1).orElseThrow(() -> new NotFoundException("config not found")));
    }

}
