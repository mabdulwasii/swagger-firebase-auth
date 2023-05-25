package com.simplifysynergy.swagger.firebase.auth.service;

import com.simplifysynergy.swagger.firebase.auth.model.SignUpDetails;
import com.simplifysynergy.swagger.firebase.auth.model.User;
import com.simplifysynergy.swagger.firebase.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashSet;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> createUser(SignUpDetails signUpDetails) {

        User user = new User();
        user.setUsername(signUpDetails.getUsername().toLowerCase());
        user.setFirstName(signUpDetails.getFirstName());
        user.setLastName(signUpDetails.getLastName());
        if (signUpDetails.getEmail() != null) {
            user.setEmail(signUpDetails.getEmail().toLowerCase());
        }

        return Flux
                .fromIterable(signUpDetails.getAuthorities() != null ? signUpDetails.getAuthorities() : new HashSet<>())
                .then(Mono.just(user))
                .publishOn(Schedulers.boundedElastic())
                .map(newUser -> {
                    String encryptedPassword = passwordEncoder.encode(signUpDetails.getPassword());
                    newUser.setPassword(encryptedPassword);
                    return newUser;
                })
                .flatMap(this::saveUser)
                .doOnNext(user1 -> log.debug("Created Information for User: {}", user1));

    }

    @Transactional
    public Mono<User> saveUser(User user) {
        return userRepository
                .save(user)
                .flatMap(Mono::just);
    }
}
