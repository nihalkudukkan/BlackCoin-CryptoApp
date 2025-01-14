package com.example.blackcoinservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockResponse {
    private long blockId;
    private String timestamp;
    private String previousHash = "";
    private String hash = "";
    private long nonce;
    private int transactions;
}
