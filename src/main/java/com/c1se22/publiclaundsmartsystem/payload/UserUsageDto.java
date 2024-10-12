package com.c1se22.publiclaundsmartsystem.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserUsageDto {
    private String userName;
    private Long usageCount;
    private String userEmail;
}
