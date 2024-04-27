package com.kutca.tcrms.common.config;

import com.kutca.tcrms.common.security.JWTAuthenticationRequestFilter;
import com.kutca.tcrms.common.security.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JWTTokenProvider jwtTokenProvider;

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
                .addFilterBefore(new JWTAuthenticationRequestFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
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
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build();
        UserDetails admin = User.withDefaultPasswordEncoder().username("admin").password("password").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    // AuthenticationManager 설정
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return new BCryptPasswordEncoder();
    }
}
