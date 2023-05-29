package com.simplifysynergy.swagger.firebase.auth.config;

import com.simplifysynergy.swagger.firebase.auth.model.AuthRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Configuration
@Slf4j
public class FirebaseAuthenticationManager implements ReactiveAuthenticationManager {
    WebClient webClient = WebClient.create("https://identitytoolkit.googleapis.com/v1");

    @Value("${app.firebase.key}")
    private String fireBaseApiKey;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.debug("Email =  {}, Password = {}", email, password);

        return webClient.post()
                .uri("/accounts:signInWithPassword?key=" + fireBaseApiKey)
                .body(Mono.just(new AuthRequest(email, password, true)), AuthRequest.class)
                .header("Content-Type", "application/json")
                .exchangeToMono(response -> {
                    log.debug("Response => " + response);
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else {
                        throw new RuntimeException("Could not login with firebase authentication");
                    }
                })
                .map(item -> new UsernamePasswordAuthenticationToken(
                        email, password, new ArrayList<>()));
    }
}
