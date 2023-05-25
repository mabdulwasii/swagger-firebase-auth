package com.simplifysynergy.swagger.firebase.auth.config;

import com.simplifysynergy.swagger.firebase.auth.security.TokenProvider;
import com.simplifysynergy.swagger.firebase.auth.security.filter.ApiFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.cors.reactive.CorsWebFilter;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private final ReactiveUserDetailsService userDetailsService;
    private final FirebaseAuthenticationManager firebaseAuthenticationManager;
    private final TokenProvider tokenProvider;
    private final CorsWebFilter corsWebFilter;

    public SecurityConfiguration(ReactiveUserDetailsService userDetailsService,
                                 FirebaseAuthenticationManager firebaseAuthenticationManager, TokenProvider tokenProvider, CorsWebFilter corsWebFilter) {
        this.userDetailsService = userDetailsService;
        this.firebaseAuthenticationManager = firebaseAuthenticationManager;
        this.tokenProvider = tokenProvider;
        this.corsWebFilter = corsWebFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public ReactiveAuthenticationManager reactiveApiAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(
                userDetailsService
        );
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
        http.csrf().disable()
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/login").permitAll()
                        .pathMatchers("/api/register").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterBefore(corsWebFilter, SecurityWebFiltersOrder.REACTOR_CONTEXT)
                .addFilterAt(new ApiFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .authenticationManager(reactiveApiAuthenticationManager())
                .authenticationManager(reactiveApiAuthenticationManager());
        return http.build();
    }

    @Bean
    SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .anyExchange().authenticated()
                .and().authenticationManager(firebaseAuthenticationManager)
                .formLogin()
                .and().build();
    }

}
