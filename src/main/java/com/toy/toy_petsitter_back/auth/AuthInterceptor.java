package com.toy.toy_petsitter_back.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.toy.toy_petsitter_back.exception.CustomException;
import com.toy.toy_petsitter_back.exception.ErrorMessage;
import com.toy.toy_petsitter_back.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Service
public class AuthInterceptor implements HandlerInterceptor {

    AuthInterceptor(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository ;


    //제일 먼저 요청이 들어오는 곳 //실행 직전에 동작하는 method. 값이 true일 경우 정상 실행. false일 경우 실행 종료(=컨트롤러 진입x)
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws CustomException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>preHandle");

        response.setHeader("Content-Type", "application/json");
        response.setHeader("charset", "utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");

        // CORS 요청 승인처리
        if (Objects.equals(request.getMethod(), "OPTIONS")) return true;

        //권한 확인 필요 x N
        //토큰 필요 x: 회원가입, 로그인, 메인, 글보기

        //권한 확인 필요
        //관리자만: 모든 관리자 메뉴 A
        //유저(일반+시터)만: 예약내역, 내정보 U
        //시터만: 내 게시글 P

        String url = request.getRequestURI();
        System.out.println(">>>>>>>>>>>>>>>url"+url);

        AuthCheck methodAnnotation = ((HandlerMethod)handler).getMethodAnnotation(AuthCheck.class);
        //(HandlerMethod)handler를 통해 현재 요청을 처리하는 핸들러 메소드(컨트롤러의 메소드)를 가져오고
        //getMethodAnnotation()를 사용하여 해당 핸들러 메소드에 사용된 AuthCheck 어노테이션을 가져온 뒤,
        //이를 methodAnnotation 변수에 할당

        //handler가 HandlerMethod타입이 아니거나 || handler에서 호출될 메서드의 어노테이션을 가져와 AuthCheck가 없거나 || AuthCheck 값이 false인 경우 //=토큰체크를 하지 않아야 하는 경우
        //회원가입이나 로그인 같이 검증이 필요 없는 경우 return true (preHandle에서 false 리턴시 요청 거부)
        if (methodAnnotation == null || methodAnnotation.role() == AuthCheck.Role.NONE ) {
            System.out.println(">>>>>>>>>>>interceptor_preHandle_토큰검사x");
            return true;
        }

        //토큰 디코딩
        DecodedJWT decodeJWT = new JwtService().getDecodedJwt(request);
        //디코딩 후 가져온 토큰에서 userKey 값을 꺼내고,
        //DB에서 조회한다 -> 만약 조회 결과가 없다면 마찬가지로 INVALID_TOKEN 에러 발생 시킴
        Integer userKey = Integer.parseInt(decodeJWT.getClaim("userKey").asString());
        if(userRepository.findUserKey(userKey) == null) {
            throw ErrorMessage.INVALID_TOKEN.getException();
        }

        String authority = decodeJWT.getClaim("authority").asString();
        System.out.println(">>>>>>>>>>>>Interceptor_authority"+authority);

        //관리자 메뉴인 경우
        if(methodAnnotation.role() == AuthCheck.Role.ADMIN) {
            System.out.println("관리자 메뉴인 경우,, ");
            if(!authority.equals("A")){ //권한이 관리자가 아닌 경우
                return false;
            }
            return true;
        }

        //모든 것이 통과 되었을 경우 true를 리턴
        return true;

    }


}
