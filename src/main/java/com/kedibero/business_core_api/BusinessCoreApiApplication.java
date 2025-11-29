package com.kedibero.business_core_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BusinessCoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessCoreApiApplication.class, args);
	}

}
