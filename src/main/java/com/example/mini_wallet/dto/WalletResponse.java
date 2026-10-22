
package com.example.mini_wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class WalletResponse {

    private Long walletId;
    private String name;
    private String email;
    private BigDecimal balance;
}
