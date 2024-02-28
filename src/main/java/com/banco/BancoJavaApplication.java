package com.banco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class BancoJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BancoJavaApplication.class, args);
	}

}
