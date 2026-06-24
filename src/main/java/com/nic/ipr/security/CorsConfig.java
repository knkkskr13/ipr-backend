package com.nic.ipr.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Which origins are allowed to call our backend
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",           // React-vite dev server
                "http://localhost:3000",           // React-server
                "https://ipr-frontend.onrender.com", // Production frontend
                "https://antitrust-browbeat-parlor.ngrok-free.dev" //ngrok live server
        ));


        // Which HTTP methods are allowed
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Which headers are allowed
        config.setAllowedHeaders(List.of("*"));

        // Allow Authorization header (needed for JWT token)
        config.setAllowCredentials(true);

        // Apply this config to ALL endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}