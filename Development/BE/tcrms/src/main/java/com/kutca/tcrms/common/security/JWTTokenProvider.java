package com.kutca.tcrms.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JWTTokenProvider {

    private String secretKey = "test1234567890test".repeat(100);
    private Long tokenExpiration = 1000 * 60 * 60L;

    private final UserDetailService userDetailService;

    public String createToken(Long id, String role){
        return Jwts.builder()
                .claims(Map.of("id", id, "role", role))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            return !Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
        } catch (SignatureException e) { // 유효한 JWT signature가 아닙니다.
            return false;
        } catch (MalformedJwtException e) { // 유효한 JWT token이 아닙니다.
            return false;
        } catch (ExpiredJwtException e) {   // 만료된 JWT token 입니다.
            return false;
        } catch (UnsupportedJwtException e) {   //  지원하지 않는 JWT token 입니다.
            return false;
        } catch (IllegalArgumentException e) { // JWT claims 문자열이 비어있습니다.
            return false;
        }
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailService.loadUserById(this.getId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Long getId(String token) {
        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("id", Long.class);
    }

}
