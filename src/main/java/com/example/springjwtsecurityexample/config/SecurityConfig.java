package com.example.springjwtsecurityexample.config;

import com.example.springjwtsecurityexample.security.SecurityAuthConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity,
                                                         ReactiveAuthenticationManager authenticationManager,
                                                         AuthenticationWebFilter tokenFilter) {

        return httpSecurity.authorizeExchange(auth -> auth.pathMatchers("/api/v1/public/**").permitAll()
                .anyExchange().authenticated())
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authenticationManager(authenticationManager)
            .addFilterAfter(tokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build();

    }

    @Bean
    public AuthenticationWebFilter tokenFilter(ReactiveAuthenticationManager authenticationManager, SecurityAuthConverter securityAuthConverter) {
        AuthenticationWebFilter bearerAuthFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthFilter.setServerAuthenticationConverter(securityAuthConverter);
        bearerAuthFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));

        return bearerAuthFilter;
    }
}
