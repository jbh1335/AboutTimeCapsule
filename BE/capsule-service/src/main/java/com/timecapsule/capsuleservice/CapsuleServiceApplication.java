package com.timecapsule.capsuleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CapsuleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapsuleServiceApplication.class, args);
	}

}
