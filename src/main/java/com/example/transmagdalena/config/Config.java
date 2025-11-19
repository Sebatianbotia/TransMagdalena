package com.example.transmagdalena.config;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.TypeAlias;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "configs")
@SQLDelete(sql = "update configs set is_delete = true where id = ?")
@Where(clause = "is_delete = false")
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ConfigType type;

    private Float value;

    private Boolean isDelete;
}
