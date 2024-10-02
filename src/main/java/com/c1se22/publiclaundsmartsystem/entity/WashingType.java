package com.c1se22.publiclaundsmartsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "washing_types")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WashingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;
    @Column(name = "type_name", nullable = false)
    private String typeName;
    @Column(name = "default_duration", nullable = false)
    private Integer defaultDuration;
    @Column(name = "default_price", precision = 10, scale = 2)
    private BigDecimal defaultPrice = BigDecimal.ZERO;
}
