package com.xm.crypto.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class CryptoRecommendationApplication {
	public static void main(String[] args) {
		SpringApplication.run(CryptoRecommendationApplication.class, args);
	}




}
