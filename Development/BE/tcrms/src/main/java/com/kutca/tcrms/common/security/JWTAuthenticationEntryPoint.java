package com.kutca.tcrms.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //  인증 실패
        response.setStatus(HttpStatus.UNAUTHORIZED.value());    //  401
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseDto.builder().isSuccess(false).message("JWT 토큰 유효기간이 만료되었습니다. 재로그인 해주세요.").build()));
    }
}
