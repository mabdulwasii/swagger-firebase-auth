package com.simplifysynergy.swagger.firebase.auth.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;

@Value
@Builder
public class Student {

    @Id
    Long id;
    String name;
}
