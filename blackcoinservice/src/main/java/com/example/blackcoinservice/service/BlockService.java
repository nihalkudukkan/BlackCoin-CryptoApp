package com.example.blackcoinservice.service;

import com.example.blackcoinservice.model.BlackBlock;
import com.example.blackcoinservice.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Stack;

@Service
public class BlockService {

    private final Stack<BlackBlock> blackBlocks = new Stack<>();

    @Autowired
    private TransactionService transactionService;

    public Stack<BlackBlock> getBlackBlocks() {
        return blackBlocks;
    }

    public void addBlock(BlackBlock blackBlock) {
        blackBlocks.add(blackBlock);
    }

    public void addNewBlockAfterMining(String publicAddress) {
        BlackBlock newBlock = new BlackBlock();
        newBlock.setBlockId(blackBlocks.size()+1);
        newBlock.setTimestamp(new Date().toString());
        newBlock.setPreviousHash(getBlackBlocks().peek().getHash());
        newBlock.setHash("");
        newBlock.setNonce(0);
        getBlackBlocks().push(newBlock);

        this.addTransaction(new Transaction("", publicAddress, "10.000", ""));
    }

    public void addTransaction(Transaction transaction) {
        BlackBlock lastBlock = this.blackBlocks.peek();
        if (lastBlock.getBlockId()==1) {
            lastBlock.getTransactions().add(transaction);
            return;
        }
        if (transaction.getFrom().isEmpty()) {
            lastBlock.getTransactions().add(transaction);
            return;
        }
        boolean isValid;
        try {
            isValid = transactionService.verifyTransaction(transaction);
        } catch (Exception e) {
            return;
        }
        if (isValid) {
            lastBlock.getTransactions().add(transaction);
        }
    }
}
