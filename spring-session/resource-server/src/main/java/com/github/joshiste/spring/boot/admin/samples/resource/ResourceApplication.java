package com.github.joshiste.spring.boot.admin.samples.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@EnableAutoConfiguration
@EnableRedisHttpSession
@RestController
public class ResourceApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}

	@RequestMapping("/")
	public String hello() {
		return "hello";
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable();
		http.authorizeRequests().antMatchers("/mgmt/health").permitAll().anyRequest()
				.authenticated();
		http.csrf().ignoringAntMatchers("/mgmt/**");
	}

}
