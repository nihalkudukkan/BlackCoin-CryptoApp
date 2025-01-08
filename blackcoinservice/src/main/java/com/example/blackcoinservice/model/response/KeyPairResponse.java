package com.example.blackcoinservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyPairResponse {
    private String privateKey;
    private String publicKey;
    private String compressedPublicKey;
}
