package com.example.mini_wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TransferRequest {

    @NotNull(message = "Sender wallet ID is required")
    private Long fromWalletId;

    @NotNull(message = "Receiver wallet ID is required")
    private Long toWalletId;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(
            value = "0.00",
            inclusive = false,
            message = "Transfer amount must be greater than zero"
    )
    private BigDecimal amount;
}
