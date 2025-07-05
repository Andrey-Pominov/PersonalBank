package com.example.personal.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PersonalBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonalBankApplication.class, args);
	}
}
