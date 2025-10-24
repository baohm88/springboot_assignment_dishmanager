package com.t2404e.dishmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI dishManagerAPI() {
        return new OpenAPI().info(new Info()
                .title("🍽️ Dish Manager API")
                .description("API quản lý Món ăn & Danh mục cho Nhà hàng — FPT Aptech Assignment")
                .version("1.0.0")
                .contact(new Contact().name("Ha Manh Bao").email("your.email@example.com"))
        );
    }
}
