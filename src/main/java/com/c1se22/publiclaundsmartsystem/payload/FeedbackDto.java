package com.c1se22.publiclaundsmartsystem.payload;

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
