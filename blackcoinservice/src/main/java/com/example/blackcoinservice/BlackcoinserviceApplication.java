package com.example.blackcoinservice;

import com.example.blackcoinservice.model.BlackBlock;
import com.example.blackcoinservice.model.Transaction;
import com.example.blackcoinservice.service.BlockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import java.util.Date;

@SpringBootApplication
@AllArgsConstructor
public class BlackcoinserviceApplication {

	private BlockService blockService;

	public static void main(String[] args) {
		SpringApplication.run(BlackcoinserviceApplication.class, args);
	}

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	RestClient restClient() {
		return RestClient.builder()
				.baseUrl("http://localhost:5000")
				.build();
	}

	@PostConstruct
	public void airDrop() {
		if (blockService.getBlackBlocks().isEmpty()) {
			BlackBlock blackBlock = new BlackBlock();
			blackBlock.setBlockId(1);
			blackBlock.setPreviousHash("");
			blackBlock.setHash("");
			blackBlock.setTimestamp(new Date().toString());
			blockService.addBlock(blackBlock);

			Transaction t1 = new Transaction("",
					"0318e15d282eea5c64bc0e3e12ee25eef25b2a14a635023a4b33bd7641359e90f0",
					"100.000",
					""
					);

			Transaction t2 = new Transaction("",
					"02b86afa7b26d5f826ca2074ea1715a29bc9d1cf52f665ac27719fc52f93a92ec0",
					"80.000",
					""
			);

			Transaction t3 = new Transaction("",
					"02fa11181e8a04dbe80d143edec6589febca6bbf3b270ed61fe39b5fc404112fe5",
					"60.000",
					""
			);

			Transaction t4 = new Transaction("",
					"030167ec837673a7101606c62078932ad1088c4413f8bb468abbb9efd877b8f041",
					"40.000",
					""
			);

			Transaction t5 = new Transaction("",
					"029926e6567d348f1e8e5e9b7af93fe2f44fb5c7c0de10b8a67d345153f15c036b",
					"90.000",
					""
			);

			try {
				blockService.addTransaction(t1);
				blockService.addTransaction(t2);
				blockService.addTransaction(t3);
				blockService.addTransaction(t4);
				blockService.addTransaction(t5);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
