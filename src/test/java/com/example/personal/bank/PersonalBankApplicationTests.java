package com.example.personal.bank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PersonalBankApplicationTests {

	@Test
	void contextLoads() {
	}

}
