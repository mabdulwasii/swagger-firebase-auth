package com.simplifysynergy.swagger.firebase.auth.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignUpDetails {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 1, max = 50)
    private String username;

    @NotBlank
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private Set<Authority> authorities;
}
