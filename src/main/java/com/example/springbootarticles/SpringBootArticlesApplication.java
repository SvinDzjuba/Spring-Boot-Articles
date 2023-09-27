package com.example.springbootarticles;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SpringBootArticlesApplication implements CommandLineRunner {
	public static void main(String[] args) { SpringApplication.run(SpringBootArticlesApplication.class, args); }

	@Override
	public void run(String... args) throws Exception {
		// Some executions here
	}
}