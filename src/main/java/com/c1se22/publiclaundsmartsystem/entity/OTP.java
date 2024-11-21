package com.c1se22.publiclaundsmartsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity(name = "otp")
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private Integer id;
    private String email;
    private String code;
    private Boolean isUsed;
    private Date expiryDate;

    public OTP() {
        this.expiryDate = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
    }
}
