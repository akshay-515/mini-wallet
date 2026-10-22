package com.example.mini_wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DepositResponse {

    private Long walletId;
    private BigDecimal previousBalance;
    private BigDecimal amount;
    private BigDecimal currentBalance;
}
