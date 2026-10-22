package com.example.mini_wallet.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DepositRequest {

    @NotNull(message = "Deposit amount is required")
    @DecimalMin(
            value = "0.00",
            inclusive = false,
            message = "Deposit amount must be greater than zero"
    )
    private BigDecimal amount;
}