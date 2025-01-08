package com.example.blackcoinservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyRequest {
    private String publicKey;
    private String privateKey;
}
