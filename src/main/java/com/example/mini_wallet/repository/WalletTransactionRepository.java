
package com.example.mini_wallet.repository;

import com.example.mini_wallet.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepository
        extends JpaRepository<WalletTransaction, Long> {

    List<WalletTransaction> findByWalletIdOrderByCreatedAtDesc(Long walletId);
}
