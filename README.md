spring-boot-admin-samples
=================

This repository contains some examples using [Spring Boot Admin](https://github.com/codecentric/spring-boot-admin).

## basic-auth
This examples shows how to add basic auth credentials when accessing the client managment endpoints without exposing them to the user.
Fixed credentials are used, which must be the same for every client application. Every user who gains access to the admin server, can acces all client applications.

To implement this you simply need to add a [ZuulFilter](https://github.com/joshiste/spring-boot-admin-samples/blob/master/basic-auth/admin-server/src/main/java/com/github/joshiste/spring/boot/admin/samples/admin/BasicAuthFilter.java) which adds the authorization header.

## spring-session
This examples uses spring session backed by redis to secure the admin server.
The client application can decide whether the user has access or not.

The code is basically taken from the great [Spring guide on using Spring Security and Angular JS](https://spring.io/guides/tutorials/spring-security-and-angular-js/#_the_api_gateway_pattern_angular_js_and_spring_security_part_iv). Only small changes are necessary from the tutorial to make this run with Spring Boot Admin.

## oauth2
This example shows how to secure your endpoints and the admin server using oauth2.
The client application can decide wheter the user has access or not.

The code is basically taken from the great [Spring guide on using Spring Security and Angular JS](https://spring.io/guides/tutorials/spring-security-and-angular-js/#_sso_with_oauth2_angular_js_and_spring_security_part_v) and only adapted so that the ui server becomes the admin server and the necessary security config to allow the health-checks.
