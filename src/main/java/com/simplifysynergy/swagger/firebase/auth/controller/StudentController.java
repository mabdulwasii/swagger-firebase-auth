package com.simplifysynergy.swagger.firebase.auth.controller;

import com.simplifysynergy.swagger.firebase.auth.model.Student;
import com.simplifysynergy.swagger.firebase.auth.repository.StudentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/student")
@Tag(name = "Student APIs", description = "Student APIs for managing students")
public class StudentController {

    private final StudentRepository studentRepository;

    @GetMapping
    @Operation(description = "Get all students")
    public Flux<Student> getAll() {
        return studentRepository.findAll();
    }

    @GetMapping(value = "/{name}")
    @Operation(description = "Get a Student by name", parameters = {
            @Parameter(name = "name", in = ParameterIn.QUERY, required = true, description = "student name")
    })
    public Mono<Student> getByName(@PathVariable String name) {
        return studentRepository.findByName(name);
    }

    @PostMapping
    @Operation(description = "Create a new student",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody())
    public Mono<Student> createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @PutMapping
    @Operation(description = "Update a new student",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody())
    public Mono<Student> updateStudent(@RequestBody Student student) {
        return studentRepository
                .findByName(student.getName())
                .flatMap(studentResult -> studentRepository.save(student));
    }

    @DeleteMapping
    @Operation(description = "Delete a student",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody())
    public Mono<Void> deleteStudent(@RequestBody Student student) {
        return studentRepository.deleteById(student.getId());
    }
}
