package com.scm.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class AppConfig {

    @Value("${CLOUDINARY_NAME}")
    private String cloudName;

    @Value("${CLOUDINARY_API_KEY}")
    private String apiKey;

    @Value("${CLOUDINARY_API_SECRET}")
    private String apiSecret;

    @PostConstruct
    public void debugEnv() {
        System.out.println("DEBUG ENV:");
        System.out.println("CLOUDINARY_NAME = " + cloudName);
        System.out.println("CLOUDINARY_API_KEY = " + apiKey);
        System.out.println("CLOUDINARY_API_SECRET = " + apiSecret);
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecret)
        );
    }
}
