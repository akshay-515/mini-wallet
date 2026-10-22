
package com.example.mini_wallet.controller;

import com.example.mini_wallet.dto.DepositResponse;
import com.example.mini_wallet.dto.TransferRequest;
import com.example.mini_wallet.dto.TransferResponse;
import com.example.mini_wallet.dto.request.CreateWalletRequest;
import com.example.mini_wallet.dto.response.WalletResponse;
import com.example.mini_wallet.service.WalletService;
import com.example.mini_wallet.dto.response.TransactionResponse;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse createWallet(
            @Valid @RequestBody CreateWalletRequest request) {

        return walletService.createWallet(request);
    }


    @GetMapping("/{walletId}")
    public WalletResponse getWalletById(
            @PathVariable Long walletId) {

        return walletService.getWalletById(walletId);
    }


    @PostMapping("/{walletId}/deposit")
    public DepositResponse deposit(
            @PathVariable Long walletId,
            @Valid @RequestBody com.example.mini_wallet.dto.request.DepositRequest request) {

        return walletService.deposit(walletId, request);
    }

    @GetMapping("/{walletId}/transactions")
    public List<TransactionResponse> getTransactionHistory(
            @PathVariable Long walletId) {

        return walletService.getTransactionHistory(walletId);
    }


    @PostMapping("/transfer")
    public TransferResponse transfer(
            @Valid @RequestBody TransferRequest request) {

        return walletService.transfer(request);
    }



}
