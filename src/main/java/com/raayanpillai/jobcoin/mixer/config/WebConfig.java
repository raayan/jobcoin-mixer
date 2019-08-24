package com.raayanpillai.jobcoin.mixer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Map /swagger for easier access
        registry.addRedirectViewController("/swagger", "/swagger-ui.html");
    }
}
