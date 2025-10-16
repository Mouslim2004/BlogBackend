package com.comorosrising.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud_name}")
    private String cloudName;
    @Value("${cloudinary.api_key}")
    private String apiKey;
    @Value("${cloudinary.api_secret}")
    private String apiSecret;


    @Bean
    public Cloudinary cloudinary(){
        System.out.println("=== Cloudinary Configuration ===");
        System.out.println("Cloud Name: " + cloudName);
        System.out.println("API Key: " + apiKey);
        System.out.println("API Secret configured: " + !apiSecret.isEmpty());

        // Test if credentials are valid (just the format)
        if (cloudName == null || cloudName.isEmpty()) {
            System.err.println("ERROR: Cloudinary cloud_name is missing!");
        }
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("ERROR: Cloudinary api_key is missing!");
        }
        if (apiSecret == null || apiSecret.isEmpty()) {
            System.err.println("ERROR: Cloudinary api_secret is missing!");
        }
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }
}
