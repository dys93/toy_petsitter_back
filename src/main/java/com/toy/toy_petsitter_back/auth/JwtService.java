package com.toy.toy_petsitter_back.auth;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.toy.toy_petsitter_back.DTO.Member;
import com.toy.toy_petsitter_back.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    private final String ISSUER = "ToyApi"; //발급주체
    private final Long EXPIRED = 60L * 30; //유효시간(30분) 60L * 30
    private final Long EXPIRED_LONG = 60L * 60 * 24; //유효기간(2주) 60L * 60 * 24 * 14
    private final String HMAC256SECRET_A = "Ks0#s.dfkG-@ksvI"; //서명에 사용할 암호키. 16자리 문자열
    private final String HMAC256SECRET_R = "dgG2*dG*_s#g^gGl"; //서명에 사용할 암호키. 16자리 문자열
    private final String JWT_HEADERKEY = "Authorization"; //토큰 이름


    //JWT 토큰 생성
    public HashMap<String, Object> createJwt(Member member) {
        System.out.println(">>>>>>>>>>>>>>>JWT 토큰 생성_createJwt");
        HashMap<String, Object> result = new HashMap<>();
        //토큰 생성 일자
        Date issuedDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        String accessToken = JWT.create()
                .withClaim("userKey", member.getUserKey()) //해당 부분을 이용해 JWT 토큰 내에 여러가지 데이터 저장 가능
                .withIssuedAt(issuedDate) //생성일자
                .withIssuer(ISSUER) //발행주체
                .withExpiresAt(Date.from(LocalDateTime.now().plusSeconds(EXPIRED).atZone(ZoneId.systemDefault()).toInstant())) //유효기간
                .sign(Algorithm.HMAC256(HMAC256SECRET_A));

        String refreshToken = JWT.create()
                .withClaim("userKey", member.getUserKey())
                .withIssuedAt(issuedDate) //생성일자
                .withIssuer(ISSUER) //발행주체
                .withExpiresAt(Date.from(LocalDateTime.now().plusSeconds(EXPIRED_LONG).atZone(ZoneId.systemDefault()).toInstant())) //유효기간
                .sign(Algorithm.HMAC256(HMAC256SECRET_R));

        System.out.println(">>>>>>>>>>>>>>>JWT 토큰 생성 accessToken"+accessToken);
        System.out.println(">>>>>>>>>>>>>>>JWT 토큰 생성 refreshToken"+refreshToken);
        System.out.println(">>>>>>>>>>>>>>>>>>>createJWTTime:"+issuedDate);

        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("issuedDate", issuedDate);

        return result;
    }

    //JWT 토큰 디코딩 with request
    @SneakyThrows
    public DecodedJWT getDecodedJwt(HttpServletRequest request) {
        String jwtToken = request.getHeader(JWT_HEADERKEY);
        System.out.println(">>>>>>>>>>>>JWT 토큰 디코딩 with request:" +jwtToken);
        if(jwtToken == null) throw ErrorMessage.INVALID_TOKEN.getException();
        if(!jwtToken.startsWith("Bearer ")) throw ErrorMessage.INVALID_TOKEN.getException();
        try {
            return JWT.require(Algorithm.HMAC256(HMAC256SECRET_A))
                    .withIssuer(ISSUER)
                    .build()
                    .verify(jwtToken.replace("Bearer ", ""));
        } catch (Exception e) {
            //AccessToken 만료시 에러 내림
            if(e.getMessage().contains("expired")) {
                System.out.println(">>>>>>>>>>AccessToken 만료 시 에러");
                throw ErrorMessage.EXPIRED_ACCESS_TOKEN.getException();
            } else {
                throw ErrorMessage.INVALID_TOKEN.getException();
            }
        }
    }

    //JWT 토큰 디코딩 with token
    @SneakyThrows
    public DecodedJWT getDecodedJwtWithToken(String jwtToken) {
        System.out.println(">>>>>>>>>>>>JWT 토큰 디코딩 with token:" +jwtToken);
        try {
            return JWT.require(Algorithm.HMAC256(HMAC256SECRET_A))
                    .withIssuer(ISSUER)
                    .build()
                    .verify(jwtToken.replace("Bearer ", ""));
        } catch (Exception e) {
            //AccessToken 만료시 에러 내림
            if(e.getMessage().contains("expired")) {
                System.out.println(">>>>>>>>>>AccessToken 만료 시 에러");
                throw ErrorMessage.EXPIRED_ACCESS_TOKEN.getException();
            } else {
                throw ErrorMessage.INVALID_TOKEN.getException();
            }
        }
    }

    //JWT 토큰 디코딩 with refresh token
    @SneakyThrows
    public DecodedJWT getDecodedJwtWithTokenR(String jwtToken) {
        System.out.println(">>>>>>>>>>>>JWT 토큰 디코딩 with refresh token:" +jwtToken);
        try {
            return JWT.require(Algorithm.HMAC256(HMAC256SECRET_R))
                    .withIssuer(ISSUER)
                    .build()
                    .verify(jwtToken.replace("Bearer ", ""));
        } catch (Exception e) {
            if(e.getMessage().contains("expired")) {
                System.out.println(">>>>>>>>>>>>RefreshToken 만료시 재로그인 에러");
                //RefreshToken 만료시 재로그인 에러
                throw ErrorMessage.EXPIRED_TOKEN.getException();
            } else {
                throw ErrorMessage.INVALID_TOKEN.getException();
            }
        }
    }


}
