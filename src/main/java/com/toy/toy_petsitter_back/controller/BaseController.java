package com.toy.toy_petsitter_back.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class BaseController {

    //파라미터 값 가져올 때 사용(null일 경우 에러발생)
    public String getParameter(String id) {
        String value = getRequest().getParameter(id);
        if(value == null) {
            //파라미터 오류 추가 필요
        }
        return value;
    }

    //파라미터 값 가져올 때 사용(null일 경우 null 반환)
    public String getParameterOrNull(String id) {
        return getRequest().getParameter(id);
    }


    //원하는 데이터 꺼낼 때 사용 //HttpServletRequest 객체 안의 getParameter() 이용
    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //RequestContextHolder = Spring 프레임워크 전 구간에서 HttpServletRequest에 접근할 수 있게 도와주는 구현체
        //getRequestAttributes = HttpServletRequest를 조회하는 메서드 //RequestAttributes가 없으면 Null을 반환
    }


}
