package com.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.rest")
public class MaterialNg3RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaterialNg3RestApplication.class, args);
	}
}
