package com.orebi.service.bank;

public interface BankService {
    void handleBankTransfer(Long orderId, String imageUrl);
    void confirmBankTransfer(Long orderId);
}
