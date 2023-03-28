package com.toy.toy_petsitter_back.auth;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.toy.toy_petsitter_back.DTO.Member;
import com.toy.toy_petsitter_back.exception.CustomException;
import com.toy.toy_petsitter_back.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtService {

    private static final String ISSUER = "YOUNJU";
    private static final Long EXPIRED = 60L * 30; //30분
    private  static final String HMAC256SECRET = "Ks0#s.dfkG-@ksvI"; //서명에 사용할 암호키. 16자리 문자열
    private final String JWT_HEADERKEY = "Authorization";
//    private final HttpServletRequest request;

//    public JwtService(HttpServletRequest request) {
//        this.request = request;
//    }

    //JWT 토큰 생성
    public String createJwt(Member member) {

        System.out.println(">>>>>>>>>>>>>>>userKey"+member.getUserKey());

        return JWT.create()
                .withClaim("userKey", member.getUserKey()) //해당 부분을 이용해 JWT 토큰 내에 여러가지 데이터 저장 가능
                .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .withClaim("ISSUER",ISSUER) //발행주체
                .withExpiresAt(Date.from(LocalDateTime.now().plusSeconds(EXPIRED).atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC256(HMAC256SECRET));
    }

    //JWT 토큰 디코딩
    public DecodedJWT getDecodedJwt(HttpServletRequest request) throws JWTDecodeException, CustomException {
        String jwtToken = request.getHeader(JWT_HEADERKEY);
        if(jwtToken == null) {
            throw ErrorMessage.INVALID_TOKEN.getException();
        }
        if(!jwtToken.startsWith("Bearer ")) {
            throw ErrorMessage.INVALID_TOKEN.getException();
        }
        try {
            return JWT.require(Algorithm.HMAC256(HMAC256SECRET))
                    .withIssuer(ISSUER)
                    .build()
                    .verify(jwtToken.replace("Bearer ", ""));
        } catch (Exception e) {
            if(e.getMessage().contains("expired")) {
                throw ErrorMessage.EXPIRED_TOKEN.getException();
            } else {
                throw ErrorMessage.INVALID_TOKEN.getException();
            }
        }
    }

}
