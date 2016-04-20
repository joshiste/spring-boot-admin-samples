package com.github.joshiste.spring.boot.admin.samples.admin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import de.codecentric.boot.admin.config.EnableAdminServer;

@Configuration
@EnableAutoConfiguration
@EnableAdminServer
public class AdminApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.formLogin().and().authorizeRequests().antMatchers(HttpMethod.POST, "/api/applications")
				.permitAll()//
				.antMatchers("/mgmt/health").permitAll()//
				.anyRequest().authenticated()//
				.and().csrf().ignoringAntMatchers("/api/**", "/mgmt/**")
				.csrfTokenRepository(csrfTokenRepository()).and()
				.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
	}

	@Bean
	@ConfigurationProperties("spring.boot.admin.auth")
	public BasicAuthFilter basicAuthFilter() {
		return new BasicAuthFilter();
	}

	private Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request,
					HttpServletResponse response, FilterChain filterChain)
							throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					String token = csrf.getToken();
					if (cookie == null || token != null && !token.equals(cookie.getValue())) {
						cookie = new Cookie("XSRF-TOKEN", token);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
				filterChain.doFilter(request, response);
			}
		};
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

}
