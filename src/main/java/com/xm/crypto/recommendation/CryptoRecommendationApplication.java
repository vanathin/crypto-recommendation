package com.xm.crypto.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaRepositories("com.xm.crypto.recommendation.persistence.repositories")
@EntityScan("com.xm.crypto.recommendation.persistence.entities")
@EnableWebMvc
public class CryptoRecommendationApplication {

	/*private final CryptoPriceService cryptoPriceService;

	@Autowired
	public CryptoRecommendationApplication(CryptoPriceService cryptoPriceService) {
		this.cryptoPriceService = cryptoPriceService;
	}*/


	/*@PostConstruct
	public void uploadCsvDataToDb() throws IOException {
		cryptoPriceService.readCsvAndUploadData();
	}*/

	public static void main(String[] args) {
		SpringApplication.run(CryptoRecommendationApplication.class, args);
	}




}
