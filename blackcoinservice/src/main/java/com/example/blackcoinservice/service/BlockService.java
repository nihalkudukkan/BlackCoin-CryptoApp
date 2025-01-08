package com.example.blackcoinservice.service;

import com.example.blackcoinservice.model.BlackBlock;
import com.example.blackcoinservice.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Stack;

@Service
@AllArgsConstructor
public class BlockService {

    private final String MINE_REWARD = "10.000";

    @Getter
    private final Stack<BlackBlock> blackBlocks = new Stack<>();

    private TransactionService transactionService;

    public void addBlock(BlackBlock blackBlock) {
        blackBlocks.add(blackBlock);
    }

    public void addNewBlockAfterMining(String publicAddress) throws Exception {
        BlackBlock newBlock = new BlackBlock();
        newBlock.setBlockId(blackBlocks.size()+1);
        newBlock.setTimestamp(new Date().toString());
        newBlock.setPreviousHash(getBlackBlocks().peek().getHash());
        newBlock.setHash("");
        newBlock.setNonce(0);
        getBlackBlocks().push(newBlock);

        this.addTransaction(new Transaction("", publicAddress, MINE_REWARD, ""));
    }

    public void addTransaction(Transaction transaction) throws Exception {
        BlackBlock lastBlock = this.blackBlocks.peek();
        // air drop for first block
        if (lastBlock.getBlockId()==1) {
            lastBlock.getTransactions().add(transaction);
            return;
        }
        // coin generation either by mining or airdrop
        if (transaction.getFrom().isEmpty()) {
            lastBlock.getTransactions().add(transaction);
            return;
        }
        // check transaction valid
        boolean isValid;
        try {
            isValid = transactionService.verifyTransaction(transaction);
        } catch (Exception e) {
            throw new Exception("Verification exception");
        }
        if (isValid) {
            lastBlock.getTransactions().add(transaction);
        }
    }
}
