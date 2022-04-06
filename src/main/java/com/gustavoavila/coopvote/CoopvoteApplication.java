package com.gustavoavila.coopvote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CoopvoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoopvoteApplication.class, args);
	}

}
