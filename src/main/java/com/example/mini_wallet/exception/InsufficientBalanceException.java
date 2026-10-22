
package com.example.mini_wallet.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(Long walletId) {
        super("Insufficient balance in wallet: " + walletId);
    }
}
