
package com.example.mini_wallet.service;

import com.example.mini_wallet.dto.DepositResponse;
import com.example.mini_wallet.dto.TransferRequest;
import com.example.mini_wallet.dto.TransferResponse;
import com.example.mini_wallet.dto.request.CreateWalletRequest;
import com.example.mini_wallet.dto.response.WalletResponse;
import com.example.mini_wallet.entity.Wallet;
import com.example.mini_wallet.entity.WalletTransaction;
import com.example.mini_wallet.exception.EmailAlreadyExistsException;
import com.example.mini_wallet.exception.InsufficientBalanceException;
import com.example.mini_wallet.repository.WalletRepository;
import com.example.mini_wallet.exception.WalletNotFoundException;
import com.example.mini_wallet.repository.WalletTransactionRepository;
import com.example.mini_wallet.dto.response.TransactionResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    @Transactional
    public WalletResponse createWallet(CreateWalletRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (walletRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new EmailAlreadyExistsException(normalizedEmail);
        }

        BigDecimal initialBalance = request.getInitialBalance() == null
                ? BigDecimal.ZERO
                : request.getInitialBalance();

        Wallet wallet = new Wallet();
        wallet.setName(request.getName().trim());
        wallet.setEmail(normalizedEmail);
        wallet.setBalance(initialBalance);

        Wallet savedWallet = walletRepository.save(wallet);

        return new WalletResponse(
                savedWallet.getId(),
                savedWallet.getName(),
                savedWallet.getEmail(),
                savedWallet.getBalance()
        );
    }

    @Transactional(readOnly = true)
    public WalletResponse getWalletById(Long walletId) {

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() ->
                        new WalletNotFoundException(walletId));

        return new WalletResponse(
                wallet.getId(),
                wallet.getName(),
                wallet.getEmail(),
                wallet.getBalance()
        );
    }


    @Transactional
    public DepositResponse deposit(Long walletId, com.example.mini_wallet.dto.request.DepositRequest request) {

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        BigDecimal amount = request.getAmount();
        BigDecimal previousBalance = wallet.getBalance();

        wallet.setBalance(previousBalance.add(amount));
        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setStatus("SUCCESS");

        walletTransactionRepository.save(transaction);

        return new DepositResponse(
                wallet.getId(),
                previousBalance,
                amount,
                wallet.getBalance()
        );
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionHistory(Long walletId) {

        if (!walletRepository.existsById(walletId)) {
            throw new WalletNotFoundException(walletId);
        }

        return walletTransactionRepository
                .findByWalletIdOrderByCreatedAtDesc(walletId)
                .stream()
                .map(TransactionResponse::from)
                .toList();
    }


    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        Long fromWalletId = request.getFromWalletId();
        Long toWalletId = request.getToWalletId();
        BigDecimal amount = request.getAmount();

        if (fromWalletId.equals(toWalletId)) {
            throw new IllegalArgumentException(
                    "Sender and receiver wallets must be different"
            );
        }

        // Lock wallets in a consistent order to reduce deadlock risk.
        Long firstId = Math.min(fromWalletId, toWalletId);
        Long secondId = Math.max(fromWalletId, toWalletId);

        Wallet firstWallet = walletRepository.findByIdForUpdate(firstId)
                .orElseThrow(() -> new WalletNotFoundException(firstId));

        Wallet secondWallet = walletRepository.findByIdForUpdate(secondId)
                .orElseThrow(() -> new WalletNotFoundException(secondId));

        Wallet sender = fromWalletId.equals(firstId)
                ? firstWallet : secondWallet;

        Wallet receiver = toWalletId.equals(firstId)
                ? firstWallet : secondWallet;

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(fromWalletId);
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        walletRepository.save(sender);
        walletRepository.save(receiver);

        String referenceId = UUID.randomUUID().toString();

        WalletTransaction outgoing = new WalletTransaction();
        outgoing.setWallet(sender);
        outgoing.setType("TRANSFER_OUT");
        outgoing.setAmount(amount);
        outgoing.setStatus("SUCCESS");
        outgoing.setReferenceId(referenceId);

        WalletTransaction incoming = new WalletTransaction();
        incoming.setWallet(receiver);
        incoming.setType("TRANSFER_IN");
        incoming.setAmount(amount);
        incoming.setStatus("SUCCESS");
        incoming.setReferenceId(referenceId);

        walletTransactionRepository.save(outgoing);
        walletTransactionRepository.save(incoming);

        return new TransferResponse(
                referenceId,
                fromWalletId,
                toWalletId,
                amount,
                "SUCCESS"
        );
    }



}
