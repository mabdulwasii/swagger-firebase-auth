package com.simplifysynergy.swagger.firebase.auth.security.domain;


import com.simplifysynergy.swagger.firebase.auth.model.User;
import com.simplifysynergy.swagger.firebase.auth.repository.UserRepository;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements ReactiveUserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserDetails> findByUsername(final String username) {
        log.debug("Authenticating {}", username);

        if (new EmailValidator().isValid(username, null)) {
            return userRepository
                    .findByEmail(username)
                    .switchIfEmpty(Mono.error(new UsernameNotFoundException("User with email " + username + " was not found in the database")))
                    .map(user -> createSpringSecurityUser(username, user));
        }

        String lowercaseUsername = username.toLowerCase(Locale.ENGLISH);
        return userRepository
                .findByUsername(lowercaseUsername)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User " + lowercaseUsername + " was not found in the database")))
                .map(user -> createSpringSecurityUser(lowercaseUsername, user));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseUsername, User user) {
        return new org.springframework.security.core.userdetails.User(lowercaseUsername, user.getPassword(), new ArrayList<>());
    }
}
