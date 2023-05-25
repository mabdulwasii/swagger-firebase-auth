package com.simplifysynergy.swagger.firebase.auth.repository;

import com.simplifysynergy.swagger.firebase.auth.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    Mono<User> findByUsername(String username);

    Mono<User> findByEmail(String email);

    Mono<Boolean> existsByUsername(String username);

    @Query("INSERT INTO user_authority VALUES(:userId, :authorityId)")
    Mono<Void> saveUserAuthority(Long userId, Long authorityId);

}
