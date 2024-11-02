package com.c1se22.publiclaundsmartsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "washing_types")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WashingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer id;
    @Column(name = "type_name", nullable = false)
    @NotBlank(message = "Type name is required")
    @NotNull(message = "Type name is required")
    private String typeName;
    @Column(name = "default_duration", nullable = false)
    @NotNull(message = "Default duration is required")
    private Integer defaultDuration;
    @Column(name = "default_price", precision = 10, scale = 2)
    @NotNull(message = "Default price is required")
    @Min(value = 0, message = "Default price must be greater than or equal to 0")
    private BigDecimal defaultPrice = BigDecimal.ZERO;
}
