package com.example.ticketing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 주소에 대해
                .allowedOrigins("*") // 모든 출처(HTML 파일 등) 허용 (실무에선 프론트 주소만 딱 적어줌)
                .allowedMethods("GET", "POST", "PUT", "DELETE"); // 허용할 HTTP 메서드
    }
}