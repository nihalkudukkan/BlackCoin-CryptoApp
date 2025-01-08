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
    private String previousHash = "";
    private String hash = "";
    private long nonce;
    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public BlackBlock clone() {
        try {
            return (BlackBlock) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
