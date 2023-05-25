package com.simplifysynergy.swagger.firebase.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.auth")
public class SecurityProperties {

    private String base64Secret;

    private long tokenValidityInSeconds;

    private long tokenValidityInSecondsForRememberMe;
}
