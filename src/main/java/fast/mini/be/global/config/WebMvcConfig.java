package fast.mini.be.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:8080",
                "http://54.79.60.180:8080",
                "http://localhost:3000",
                "https://localhost:3000",
                "https://127.0.0.1:3000"
            )
            .allowedMethods("GET", "POST");
    }
}

