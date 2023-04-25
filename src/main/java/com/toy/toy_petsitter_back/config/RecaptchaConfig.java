package com.toy.toy_petsitter_back.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class RecaptchaConfig {
    private String site;
    private String secret;
    private String url;
}
