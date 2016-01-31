spring-boot-admin-samples
=================

This repository contains some examples using [Spring Boot Admin](https://github.com/codecentric/spring-boot-admin).

## spring-session
This examples uses spring session backed by redis to secure the admin server.
The code is basically taken from the great [Spring guide on using Spring Security and Angular JS](https://spring.io/guides/tutorials/spring-security-and-angular-js/#_the_api_gateway_pattern_angular_js_and_spring_security_part_iv). Only small changes are necessary from the tutorial to make this run with Spring Boot Admin.

## oauth2
This example shows how to secure your endpoints and the admin server using oauth2.
The code is basically taken from the great [Spring guide on using Spring Security and Angular JS](https://spring.io/guides/tutorials/spring-security-and-angular-js/#_sso_with_oauth2_angular_js_and_spring_security_part_v) and only adapted so that the ui server becomes the admin server and the necessary security config to allow the health-checks.

