
package com.example.mini_wallet.exception;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(Long walletId) {
        super("Wallet not found with ID: " + walletId);
    }
}
