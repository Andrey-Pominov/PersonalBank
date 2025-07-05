package com.example.personal.bank;

import org.springframework.boot.SpringApplication;

public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.from(PersonalBankApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
