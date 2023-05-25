package com.simplifysynergy.swagger.firebase.auth.controller;

import com.simplifysynergy.swagger.firebase.auth.model.LoginDetails;
import com.simplifysynergy.swagger.firebase.auth.model.SignUpDetails;
import com.simplifysynergy.swagger.firebase.auth.model.Token;
import com.simplifysynergy.swagger.firebase.auth.model.User;
import com.simplifysynergy.swagger.firebase.auth.security.TokenProvider;
import com.simplifysynergy.swagger.firebase.auth.security.filter.ApiFilter;
import com.simplifysynergy.swagger.firebase.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final TokenProvider tokenProvider;
    private final ReactiveAuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/login")
    public Mono<ResponseEntity<Token>> login(@Valid @RequestBody Mono<LoginDetails> loginDetails) {

        log.info("User Login: {} ", loginDetails);

        return loginDetails
                .flatMap(details ->
                        authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(details.getUsername(), details.getPassword()))
                                .flatMap(auth -> Mono.fromCallable(() -> tokenProvider.createToken(auth, details.isRememberMe())))
                )
                .map(jwt -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add(ApiFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
                    return new ResponseEntity<>(new Token(jwt), httpHeaders, HttpStatus.OK);
                });
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> registerAccount(@Valid @RequestBody SignUpDetails signUpDetails) {
        log.debug("REST request to save User : {}", signUpDetails);
        return userService.createUser(signUpDetails);
    }
}
