package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmartApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmartApplication.class, args);
		System.out.println("Hello from git's development branch");
	}
}