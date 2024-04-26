package com.kutca.tcrms.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthenticationRequestFilter extends OncePerRequestFilter {

    private final JWTTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = parseJWT(request);

        if(token != null && jwtTokenProvider.validateToken(token)){

            // 아니면 UserDetails로 user 정보를 id 기반으로 db에서 가져오고
            // 이걸로 UsernamePasswordAuthenticationToken 생성함 -> 이건 outh

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
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
