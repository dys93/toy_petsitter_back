package com.toy.toy_petsitter_back.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.toy.toy_petsitter_back.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class BaseService {


    public Integer getUserKey() {
        //디코딩 후 가져온 토큰에서 userKey 값을 꺼내고,
        Integer userKey = Integer.parseInt(getToken().getClaim("userKey").asString());
        System.out.println(">>>>>>>>>>>>>>>>>>>>BaseService_getUserKey():"+userKey);
        return userKey;
    }

    //원하는 데이터 꺼낼 때 사용 //HttpServletRequest 객체 안의 getHeader() 이용
    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    private DecodedJWT getToken() {
        DecodedJWT decodeJWT = new JwtService().getDecodedJwtWithToken(getRequest().getHeader("Authorization"));
        return decodeJWT;
    }




}
