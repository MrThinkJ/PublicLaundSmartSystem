package com.c1se22.publiclaundsmartsystem.payload;

import com.c1se22.publiclaundsmartsystem.enums.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
  private Integer id;
  private BigDecimal amount;
  private TransactionStatus status;
  private LocalDateTime timestamp;
  private Integer userId;
  private String userName;
  private Integer machineId;
  private String machineName;
}
