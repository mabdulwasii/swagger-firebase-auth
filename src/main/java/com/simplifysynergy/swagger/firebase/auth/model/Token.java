package com.simplifysynergy.swagger.firebase.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Token {
    private String idToken;

    @JsonProperty("tokenId")
    String getIdToken() {
        return idToken;
    }
}
