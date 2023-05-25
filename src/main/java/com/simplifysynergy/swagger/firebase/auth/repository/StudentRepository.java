package com.simplifysynergy.swagger.firebase.auth.repository;

import com.simplifysynergy.swagger.firebase.auth.model.Student;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StudentRepository extends R2dbcRepository<Student, Long> {
    Mono<Student> findByName(String name);
}
