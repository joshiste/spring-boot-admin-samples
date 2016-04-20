package com.github.joshiste.spring.boot.admin.samples.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@EnableAutoConfiguration
@RestController
public class ResourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}

	@RequestMapping("/")
	public String hello() {
		return "hello";
	}

}
