package com.example.blackcoinservice.service;

import com.example.blackcoinservice.model.Transaction;
import com.example.blackcoinservice.model.request.VerifyRequest;
import com.example.blackcoinservice.model.response.VerifyResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionService {

    /**
    Verifies transaction as well as verifies public key. Returns true if valid.
     @param transaction transaction ro be verified
     */
    public boolean verifyTransaction(Transaction transaction) throws Exception {

        RestClient restClient = RestClient.builder().baseUrl("http://localhost:5000").build();

        VerifyRequest request = new VerifyRequest(transaction.getFrom(),
                transaction.getFrom()+transaction.getTo()+transaction.getAmount(),
                transaction.getSignature());

        VerifyResponse isValid = restClient.post()
                .uri("/verify")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .retrieve()
                .body(VerifyResponse.class);

        return isValid.getIsValid();
    }

}