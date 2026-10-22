
package com.example.mini_wallet;

import com.example.mini_wallet.dto.TransferRequest;
import com.example.mini_wallet.dto.TransferResponse;
import com.example.mini_wallet.entity.Wallet;
import com.example.mini_wallet.repository.WalletRepository;
import com.example.mini_wallet.service.WalletService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:wallet_test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class WalletTransferIntegrationTest {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletService walletService;

    @Test
    void transferShouldPreserveTotalBalance() {

        Wallet sender = createWallet(
                "Rahul",
                "rahul-test@example.com",
                "1000.00"
        );

        Wallet receiver = createWallet(
                "Priya",
                "priya-test@example.com",
                "500.00"
        );

        TransferRequest request = new TransferRequest();
        request.setFromWalletId(sender.getId());
        request.setToWalletId(receiver.getId());
        request.setAmount(new BigDecimal("300.00"));

        TransferResponse response = walletService.transfer(request);

        Wallet updatedSender = walletRepository
                .findById(sender.getId())
                .orElseThrow();

        Wallet updatedReceiver = walletRepository
                .findById(receiver.getId())
                .orElseThrow();

        assertEquals(
                0,
                new BigDecimal("700.00")
                        .compareTo(updatedSender.getBalance())
        );

        assertEquals(
                0,
                new BigDecimal("800.00")
                        .compareTo(updatedReceiver.getBalance())
        );

        BigDecimal total = updatedSender.getBalance()
                .add(updatedReceiver.getBalance());

        assertEquals(
                0,
                new BigDecimal("1500.00").compareTo(total)
        );

        assertEquals("SUCCESS", response.getStatus());
    }

    private Wallet createWallet(
            String name,
            String email,
            String balance) {

        Wallet wallet = new Wallet();
        wallet.setName(name);
        wallet.setEmail(email);
        wallet.setBalance(new BigDecimal(balance));

        return walletRepository.saveAndFlush(wallet);
    }

    @Test
    void transferShouldFailWhenSenderHasInsufficientBalance() {

        Wallet sender = createWallet(
                "Rahul",
                "rahul-insufficient@example.com",
                "100.00"
        );

        Wallet receiver = createWallet(
                "Priya",
                "priya-insufficient@example.com",
                "500.00"
        );

        TransferRequest request = new TransferRequest();
        request.setFromWalletId(sender.getId());
        request.setToWalletId(receiver.getId());
        request.setAmount(new BigDecimal("300.00"));

        assertThrows(
                RuntimeException.class,
                () -> walletService.transfer(request)
        );

        Wallet updatedSender = walletRepository
                .findById(sender.getId())
                .orElseThrow();

        Wallet updatedReceiver = walletRepository
                .findById(receiver.getId())
                .orElseThrow();

        assertEquals(
                0,
                new BigDecimal("100.00")
                        .compareTo(updatedSender.getBalance())
        );

        assertEquals(
                0,
                new BigDecimal("500.00")
                        .compareTo(updatedReceiver.getBalance())
        );
    }
}
