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

        //권한 확인 필요 x
        //토큰 필요 x: 회원가입, 로그인, 메인, 글보기 N

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

        //어노테이션을 붙이지 않았거나 || role이 NONE인 경우
        //토큰 검증이 필요 없는 경우 return true (preHandle에서 false 리턴시 요청 거부)
        if (methodAnnotation == null || methodAnnotation.role() == AuthCheck.Role.NONE ) {
            System.out.println(">>>>>>>>>>>interceptor_preHandle_토큰검사x");
            return true;
        }

        //토큰 디코딩
        DecodedJWT decodeJWT = new JwtService().getDecodedJwt(request);
        Integer userKey = Integer.parseInt(decodeJWT.getClaim("userKey").asString());
        String issuedDate = decodeJWT.getIssuedAt().toString();

        //DB에서 조회 결과가 없다면 INVALID_TOKEN 에러 발생
        if(userRepository.findUserKey(userKey) == null) {
            throw ErrorMessage.INVALID_TOKEN.getException();
        }

        //DB의 issuedDate와 디코딩한 issuedDate와 같지 않다면(재로그인의 경우) 에러 발생
        if(!userRepository.selectIssuedDate(userKey).equals(issuedDate)) {
            throw ErrorMessage.DUPLICATION_LOGIN.getException();
        }

        //userKey로 DB에서 권한 가져옴
        String authority = userRepository.selectAuthority(userKey);
        System.out.println(">>>>>>>>>>>>Interceptor_authority"+authority);


        //관리자 메뉴인 경우
        if(methodAnnotation.role() == AuthCheck.Role.ADMIN) {
            System.out.println("관리자만 접근 가능한 경우");
            if(!authority.equals("A")){ //권한이 관리자가 아닌 경우, 권한 불일치 에러 발생
                throw ErrorMessage.UNMATCHED_AUTHORITY.getException();
            }
        }

        //유저 메뉴인 경우
        if(methodAnnotation.role() == AuthCheck.Role.USER) {
            System.out.println("유저만 접근 가능한 경우");
            if(!authority.equals("U") && !authority.equals("P")){ //권한이 유저가 아닌 경우, 권한 불일치 에러 발생
                throw ErrorMessage.UNMATCHED_AUTHORITY.getException();
            }
        }
        //모든 것이 통과 되었을 경우 true를 리턴
        return true;

    }


}
