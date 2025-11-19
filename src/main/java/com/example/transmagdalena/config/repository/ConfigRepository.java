package com.example.transmagdalena.config.repository;

import com.example.transmagdalena.config.Config;
import com.example.transmagdalena.config.ConfigType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigRepository  extends JpaRepository<Config,Long> {

    Boolean existsConfigByType(ConfigType type);
    void deleteConfigByType(ConfigType type);
    Optional<Config> findConfigByType(ConfigType type);
}
