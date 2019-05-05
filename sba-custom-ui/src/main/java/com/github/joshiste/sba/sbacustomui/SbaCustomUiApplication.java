package com.github.joshiste.sba.sbacustomui;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAdminServer
public class SbaCustomUiApplication {

	@Bean
	public CustomEndpoint customEndpoint() {
		return new CustomEndpoint();
	}

	public static void main(String[] args) {
		SpringApplication.run(SbaCustomUiApplication.class, args);
	}

}
