package com.example.blackcoinservice.utilities;

import com.example.blackcoinservice.model.BlackBlock;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@Component
@AllArgsConstructor
public class HashUtility {
    ObjectMapper objectMapper;

    public String calculateHash(BlackBlock blackBlock) throws Exception {
        String blackBlockString = objectMapper.writeValueAsString(blackBlock);
        byte[] blackBlockByte = blackBlockString.getBytes();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(blackBlockByte);

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }
}
