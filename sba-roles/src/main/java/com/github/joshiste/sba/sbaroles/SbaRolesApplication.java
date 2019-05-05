package com.github.joshiste.sba.sbaroles;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.web.client.InstanceExchangeFilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@SpringBootApplication
@EnableAdminServer
public class SbaRolesApplication {

  private static final Logger log = LoggerFactory.getLogger(SbaRolesApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(SbaRolesApplication.class, args);
  }

  @Bean
  public InstanceExchangeFilterFunction auditLog() {
    return (instance, request, next) -> next.exchange(request).doOnSubscribe(s -> {
      if (HttpMethod.DELETE.equals(request.method()) || HttpMethod.POST.equals(request.method())) {
        log.info("{} for {} on {}", request.method(), instance.getId(), request.url());
      }
    });
  }

  @Configuration
  protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AdminServerProperties adminServer;

    public SecurityConfiguration(AdminServerProperties adminServer) {
      this.adminServer = adminServer;
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
      var techUserActuator = User.withUsername("tech-user-actuator").password("{noop}mystery").roles("ACTUATOR").build();
      var techUserRegister = User.withUsername("tech-user-register").password("{noop}secret").roles("SBA-CLIENT").build();
      var readOnlyUser = User.withUsername("user").password("{noop}user").roles("SBA-READ").build();
      var adminUser = User.withUsername("admin").password("{noop}admin").roles("SBA-READ", "SBA-ADMIN").build();
      return new InMemoryUserDetailsManager(techUserActuator, techUserRegister, readOnlyUser, adminUser);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      // @formatter:off
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

        http.authorizeRequests()
            .antMatchers(this.adminServer.path("/assets/**")).permitAll()
            .antMatchers(this.adminServer.path("/login")).permitAll()
            //Allow reading/writing endpoints for role ACTUATOR
            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
            //Allow register/removing instances for role SBA-CLIENT and SBA-ADMIN
            .antMatchers(HttpMethod.POST, this.adminServer.path("/instances")).hasAnyRole("SBA-CLIENT", "SBA-ADMIN")
            .antMatchers(HttpMethod.DELETE, this.adminServer.path("/instances")).hasAnyRole("SBA-CLIENT", "SBA-ADMIN")
            //Allow to change log-levels for role SBA-READ
            .antMatchers(HttpMethod.POST, this.adminServer.path("/instances/**/actuator/loggers/**")).hasAnyRole("SBA-READ")
            //Allow reading for role SBA-READ
            .antMatchers(HttpMethod.GET, this.adminServer.path("**")).hasAnyRole("SBA-READ")
            //Allow writing only for users with role SBA-ADMIN
            .antMatchers(HttpMethod.POST, this.adminServer.path("**")).hasAnyRole("SBA-ADMIN")
            .and()
        .formLogin().loginPage(this.adminServer.path("/login")).successHandler(successHandler).and()
        .logout().logoutUrl(this.adminServer.path("/logout")).and()
        .httpBasic().and()
        .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringAntMatchers(
                this.adminServer.path("/instances"),
                this.adminServer.path("/actuator/**")
            );
    // @formatter:on
    }
  }
}
