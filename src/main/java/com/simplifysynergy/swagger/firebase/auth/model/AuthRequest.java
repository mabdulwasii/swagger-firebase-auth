package com.simplifysynergy.swagger.firebase.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthRequest {
    private String email;
    private String password;
    private boolean returnSecureToken = true;
}
