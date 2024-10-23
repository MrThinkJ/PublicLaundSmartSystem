package com.c1se22.publiclaundsmartsystem.payload;

import com.c1se22.publiclaundsmartsystem.entity.Machine;
import com.c1se22.publiclaundsmartsystem.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FeedbackDto {
    private Integer id;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private Integer userId;
    private Integer machineId;
}
