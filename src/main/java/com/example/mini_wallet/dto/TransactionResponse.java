
package com.example.mini_wallet.dto.response;

import com.example.mini_wallet.entity.WalletTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TransactionResponse {

    private Long transactionId;
    private String type;
    private BigDecimal amount;
    private String status;
    private String referenceId;
    private LocalDateTime createdAt;

    public static TransactionResponse from(WalletTransaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getReferenceId(),
                transaction.getCreatedAt()
        );
    }
}
