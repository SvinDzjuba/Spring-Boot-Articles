package com.example.springbootarticles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.context.annotation.Import;
import org.springdoc.core.SwaggerUiConfigParameters;

@SpringBootApplication
@EnableMongoRepositories
@Import(SwaggerUiConfigParameters.class)
public class SpringBootArticlesApplication extends SpringBootServletInitializer {
	public static void main(String[] args) { SpringApplication.run(SpringBootArticlesApplication.class, args); }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpringBootArticlesApplication.class);
	}
}