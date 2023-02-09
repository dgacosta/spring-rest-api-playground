package com.playground.application;

import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class SpringRestApiPlaygroundApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void initialTest(){
		assertThat(1).isEqualTo(1);
	}

}
