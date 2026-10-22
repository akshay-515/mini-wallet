package com.example.mini_wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransferResponse {

    private String referenceId;
    private Long fromWalletId;
    private Long toWalletId;
    private BigDecimal amount;
    private String status;
}
