package com.example.blackcoinservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlackBlock implements Cloneable {
    private long blockId;
    private String timestamp;
    private String previousHash;
    private String hash;
    private long nonce;
    private List<Transaction> transactions = new ArrayList<>();

//    public void addTransaction(Transaction transaction) {
//        TransactionService transactionService = new TransactionService();
//        boolean isValid;
//        if (blockId==1) {
//            transactions.add(transaction);
//            return;
//        }
//        if (transaction.getFrom().isEmpty()) {
//            transactions.add(transaction);
//            return;
//        }
//        try {
//            isValid = transactionService.verifyTransaction(transaction);
//        } catch (Exception e) {
//            return;
//        }
//        if (isValid) {
//            transactions.add(transaction);
//        }
//    }

    @Override
    public BlackBlock clone() {
        try {
            BlackBlock clone = (BlackBlock) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
