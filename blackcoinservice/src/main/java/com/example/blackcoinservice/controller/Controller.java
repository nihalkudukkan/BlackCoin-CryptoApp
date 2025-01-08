package com.example.blackcoinservice.controller;

import com.example.blackcoinservice.model.BlackBlock;
import com.example.blackcoinservice.model.Transaction;
import com.example.blackcoinservice.model.request.MineRequest;
import com.example.blackcoinservice.model.request.TransactionRequest;
import com.example.blackcoinservice.model.response.HashResponse;
import com.example.blackcoinservice.service.BlockService;
import com.example.blackcoinservice.service.TransactionService;
import com.example.blackcoinservice.utilities.AmountUtility;
import com.example.blackcoinservice.utilities.HashUtility;
import com.example.blackcoinservice.utilities.KeyUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class Controller {

    private BlockService blockService;
    TransactionService transactionService;
    HashUtility hashUtility;
    KeyUtility keyUtility;
    AmountUtility amountUtility;

    @GetMapping("/allblock")
    public List<BlackBlock> getAllBlock() {
        return blockService.getBlackBlocks();
    }

    @PostMapping("/hash")
    public HashResponse hash(@RequestBody BlackBlock blackBlock) throws Exception {
        blackBlock.setHash("");

        String hexString = hashUtility.calculateHash(blackBlock);

        return new HashResponse(hexString);
    }

    @PostMapping("/mine")
    public ResponseEntity<?> mine(@RequestBody MineRequest mineRequest) throws Exception {
        long blockId = mineRequest.getBlockId();
        if (blockService.getBlackBlocks().size()>blockId) {
            return new ResponseEntity<>("already mined block", HttpStatus.BAD_REQUEST);
        }
//        BlackBlock blackBlockOnChain = blockService.getBlackBlocks().stream().filter(block -> block.getBlockId() == blockId).findFirst().orElse(null);
        BlackBlock blackBlockOnChain = blockService.getBlackBlocks().peek();
        if (blackBlockOnChain==null) {
            return new ResponseEntity<>("block not found", HttpStatus.BAD_REQUEST);
        }
        if (blackBlockOnChain.getBlockId()!=mineRequest.getBlockId()) {
            return new ResponseEntity<>("block id miss match", HttpStatus.BAD_REQUEST);
        }
        if (blackBlockOnChain.getNonce()>0 || !blackBlockOnChain.getHash().equalsIgnoreCase("")) {
            return new ResponseEntity<>("nonce already added", HttpStatus.BAD_REQUEST);
        }
        // if all check passes, make clone
        BlackBlock blackBlockClone = blackBlockOnChain.clone();
        // validate public address
        if (!keyUtility.isValidPublicKey(mineRequest.getMinerPublicAddress())) {
            return new ResponseEntity<>("invalid public key", HttpStatus.BAD_REQUEST);
        }
        //validate nonce
        blackBlockClone.setNonce(mineRequest.getNonce());
        String minedHash = hashUtility.calculateHash(blackBlockClone);

        if (!minedHash.startsWith("000")) {
            return new ResponseEntity<>("Mine challenge failed", HttpStatus.BAD_REQUEST);
        }
        blockService.getBlackBlocks().peek().setNonce(mineRequest.getNonce());
        blockService.getBlackBlocks().peek().setHash(minedHash);

        blockService.addNewBlockAfterMining(mineRequest.getMinerPublicAddress());
        return new ResponseEntity<>("Mined", HttpStatus.OK);
    }

    @GetMapping("/balance")
    public double checkBalance(@RequestParam String publicKey) {
        double balance = 0;

        for (BlackBlock block : blockService.getBlackBlocks()) {
            if (!block.getHash().isEmpty()) {
                for (Transaction transaction : block.getTransactions()) {
                    if (transaction.getFrom().equalsIgnoreCase(publicKey)) {
                        balance=balance-Double.parseDouble(transaction.getAmount());
                    }
                    if (transaction.getTo().equalsIgnoreCase(publicKey)) {
                        balance=balance+Double.parseDouble(transaction.getAmount());
                    }
                }
            } else
                // double-checking if it's the last block.
                if (block.getBlockId()==blockService.getBlackBlocks().size() && block.getBlockId()==blockService.getBlackBlocks().peek().getBlockId()) {
                for (Transaction transaction : block.getTransactions()) {
                    if (transaction.getFrom().equalsIgnoreCase(publicKey)) {
                        balance=balance-Double.parseDouble(transaction.getAmount());
                    }
                }
            }
        }

        return balance;
    }

    @PostMapping("/addtransaction")
    public ResponseEntity<?> addTransaction(@RequestBody TransactionRequest transactionRequest) {
        // check public keys
        if (!keyUtility.isValidPublicKey(transactionRequest.getFrom()) || !keyUtility.isValidPublicKey(transactionRequest.getTo())) {
            return new ResponseEntity<>("Invalid public key", HttpStatus.BAD_REQUEST);
        }
        if (transactionRequest.getFrom().equalsIgnoreCase(transactionRequest.getTo())) {
            return new ResponseEntity<>("From and To cannot be same", HttpStatus.BAD_REQUEST);
        }
        // validate and format amount
        if (!amountUtility.validateAmount(transactionRequest.getAmount())) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.BAD_REQUEST);
        }

        String adjustedAmount = amountUtility.formatAmount(transactionRequest.getAmount());

        double balance = checkBalance(transactionRequest.getFrom());

        if (balance<=Double.parseDouble(adjustedAmount)) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.BAD_REQUEST);
        }
        // create transaction object
        Transaction transaction = new Transaction();
        transaction.setFrom(transactionRequest.getFrom());
        transaction.setTo(transactionRequest.getTo());
        transaction.setAmount(adjustedAmount);
        transaction.setSignature(transactionRequest.getSignature());

        try {
            blockService.addTransaction(transaction);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Transaction added", HttpStatus.CREATED);
    }

}