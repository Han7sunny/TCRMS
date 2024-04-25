package com.kutca.tcrms.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthenticationRequestFilter extends OncePerRequestFilter {

    private final JWTTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = parseJWT(request);

        if(token != null && jwtTokenProvider.validateToken(token)){

            //  토큰 추출

            //  토큰 유효성 검사
            //  JWTTokenProvier에서 검사

            //  유효한 토큰 SecurityContextHolder를 통해 Authentication 객체 생성
            //  filterChain에 해당 토큰 filter 추가
        }
        filterChain.doFilter(request, response);
    }

    private String parseJWT(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }
}
