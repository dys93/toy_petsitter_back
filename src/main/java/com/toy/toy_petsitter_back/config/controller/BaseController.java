package com.toy.toy_petsitter_back.config.controller;

import com.toy.toy_petsitter_back.exception.ErrorMessage;
import com.toy.toy_petsitter_back.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class BaseController extends LogService {

    //파라미터 값 가져올 때 사용(null일 경우 에러발생)
    @SneakyThrows
    public String getParameter(String id) {
        System.out.println(">>>>>>>>몇번쨴지 확인용");
        String value = getRequest().getParameter(id);
        if(value == null) {
            throw ErrorMessage.PARAMETER_NONE.getException();
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
