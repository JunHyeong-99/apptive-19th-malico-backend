package com.apptive.marico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("server.cla6sha.de:80", "http://server.cla6sha.de/", "http://localhost:5173/", "http://127.0.0.1:5173/"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        config.addExposedHeader("Set-Cookie");

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
