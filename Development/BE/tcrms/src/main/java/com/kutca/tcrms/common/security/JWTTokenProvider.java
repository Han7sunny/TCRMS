package com.kutca.tcrms.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import java.util.Date;


@RequiredArgsConstructor
public class JWTTokenProvider {

//    private final UserDetailsService;
    private String secretKey = "test1234567890";
    private Long tokenExpiration = 1000L;

    public String createToken(Long id, String role){
        return Jwts.builder()
                .claim("id", Long.toString(id))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
//                .signWith(SignatureAlgorithm, secretKey)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            //  최근 버전은 claim 더이상 지원 안 하는 듯
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
//            return !claims.getBody().getExpiration().before(new Date()); // 만료기간 검사
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
