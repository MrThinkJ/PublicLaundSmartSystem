package com.c1se22.publiclaundsmartsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "machine_settings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MachineSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Integer id;
    @Column(name = "setting_name", nullable = false)
    private String settingName;
    @Column(name = "setting_value", nullable = false)
    private String settingValue;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private Machine machine;

}
