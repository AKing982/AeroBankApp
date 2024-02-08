package com.example.aerobankapp.configuration;

import com.example.aerobankapp.workbench.security.authentication.CsrfTokenLogger;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
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
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

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

                registry.addMapping("/csrf/token")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET")
                        .allowedHeaders("*")
                        .allowCredentials(true);

                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };

    }


}
