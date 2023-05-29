package com.simplifysynergy.swagger.firebase.auth.model;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {

    private String code = "01";
    private String message;
    private String details;
}
