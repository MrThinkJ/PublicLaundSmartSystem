package com.c1se22.publiclaundsmartsystem.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePaymentLinkRequestBody {
    private String productName;
    private String description;
    private int price;
    private Integer userId;
    private Integer machineId;
}
