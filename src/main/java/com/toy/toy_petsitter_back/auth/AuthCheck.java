package com.toy.toy_petsitter_back.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 인터셉트에서 권한에 따라 토큰 확인 등의 처리를 위해 생성
 * @Authcheck 어노테이션의 roll 확인
 */
@Target(ElementType.METHOD) //어노테이션이 표시될 위치//적용대상
@Retention(RetentionPolicy.RUNTIME) //어노테이션의 유지 범위//대상
public @interface AuthCheck {
//    boolean isCheck() default true;

    Role role();

    enum Role{

        NONE("N"), //토큰 없이 접근 가능
        USER("U"), //유저(일반회원)
        PET_SITTER("P"),//시터회원
        ADMIN("A");//관리자

        private final String code;

        Role(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

    }


}
