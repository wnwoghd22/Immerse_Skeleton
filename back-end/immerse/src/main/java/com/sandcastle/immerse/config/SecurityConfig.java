package com.sandcastle.immerse.config;

import com.sandcastle.immerse.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserServiceImpl userServiceImpl;
    private String secretKey = "testKey";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                .antMatchers("/user/signup", "/user/signin").permitAll()
                .antMatchers("/user/check/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt를 사용하는 경우
                .and()
                .addFilterBefore(new JwtFilter(userServiceImpl, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
