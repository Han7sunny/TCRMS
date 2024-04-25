package com.kutca.tcrms.common.config;

import com.kutca.tcrms.common.security.JWTAuthenticationRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // HttpSecurity 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/user/**").hasRole("USER")
                        .requestMatchers("/api/login").permitAll()
                        .anyRequest().authenticated())
                // id, pw 인증 필터 이전에 JWT Token 필터 추가
//                .addFilterBefore(new JWTAuthenticationRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                            ;

        return http.build();
    }

    // WebSecurity 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**");
    }

    // UserDetailsService 설정
    //  AuthenticationManager에서 계정 정보 조회하기 위함
    //  UserDetailsManager 또는 InMemoryUserDetailsManager

    // AuthenticationManager 설정
}
