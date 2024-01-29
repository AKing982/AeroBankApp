package com.example.aerobankapp.configuration;

import com.example.aerobankapp.workbench.security.authentication.CsrfTokenLogger;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class WebConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .cors(Customizer.withDefaults())
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                    //    .requestMatchers("/api/auth/login").permitAll()
                    //    .requestMatchers("/api/accounts/{user}").permitAll()
                    //    .requestMatchers("/api/profile/data/{user}").permitAll()
                               .anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);

                registry.addMapping("/csrf/token")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET")
                        .allowedHeaders("*")
                        .allowCredentials(true);

                registry.addMapping("/api/profile/data/{user}")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET")
                        .allowedHeaders("*")
                        .allowCredentials(true);

                registry.addMapping("/api/accounts/data/{user}")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET")
                        .allowedHeaders("*")
                        .allowCredentials(true);

                registry.addMapping("/api/accounts/data/codes/{user}")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET")
                        .allowedHeaders("*")
                        .allowCredentials(true);

                registry.addMapping("/api/deposits/create")
                        .allowedOrigins("http:localhost:3000")
                        .allowedMethods("POST", "GET")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };

    }


}
