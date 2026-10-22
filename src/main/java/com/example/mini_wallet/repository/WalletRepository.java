
package com.example.mini_wallet.repository;

import com.example.mini_wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<Wallet> findByEmailIgnoreCase(String email);
}
