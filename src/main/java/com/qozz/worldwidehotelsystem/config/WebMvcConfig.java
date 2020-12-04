package com.qozz.worldwidehotelsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("Content-Type", "Access-Control-Allow-Headers", "Authorization", "X-Requested-With")
                .allowedOrigins("http://localhost:4200", "https://dashboard.heroku.com/apps/hotel-system-f")
                .exposedHeaders("Content-Type", "Access-Control-Allow-Headers", "Authorization", "X-Requested-With")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

}
