package com.example.blackcoinservice.utilities;

import com.example.blackcoinservice.model.request.KeyRequest;
import com.example.blackcoinservice.model.response.KeyResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@AllArgsConstructor
public class KeyUtility {

    private RestClient restClient;

    /**
     * Calls api to check validity of public key.
     * @param publicKey EC public key
     * @return true if valid else false
     */
    public boolean isValidPublicKey(String publicKey) {
        if (publicKey.isBlank()) {
            return false;
        }
        KeyRequest keyRequest = new KeyRequest();
        keyRequest.setPublicKey(publicKey);
        KeyResponse keyResponse = restClient.post()
                .uri("/publickeycheck")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(keyRequest)
                .retrieve()
                .body(KeyResponse.class);
        return keyResponse.isValid();
    }

}
