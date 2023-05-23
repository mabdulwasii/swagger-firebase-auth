package com.simplifysynergy.swagger.firebase.auth.controller;

import com.simplifysynergy.swagger.firebase.auth.model.Student;
import com.simplifysynergy.swagger.firebase.auth.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentRepository studentRepository;

    @GetMapping
    public Flux<Student> getAll() {
        return studentRepository.findAll();
    }

    @GetMapping(value = "/{name}")
    public Mono<Student> getByName(@PathVariable String name) {
        return studentRepository.findByName(name);
    }

    @PostMapping
    public Mono<Student> createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @PutMapping
    public Mono<Student> updateStudent(@RequestBody Student student) {
        return studentRepository
                .findByName(student.getName())
                .flatMap(studentResult -> studentRepository.save(student));
    }

    @DeleteMapping
    public Mono<Void> deleteStudent(@RequestBody Student student) {
        return studentRepository.deleteById(student.getId());
    }
}
