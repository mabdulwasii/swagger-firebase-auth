package com.simplifysynergy.swagger.firebase.auth.repository;


import com.simplifysynergy.swagger.firebase.auth.model.Authority;
import com.simplifysynergy.swagger.firebase.auth.model.AuthorityType;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends R2dbcRepository<Authority, Long> {
    Mono<Authority> findByName(AuthorityType name);
}
