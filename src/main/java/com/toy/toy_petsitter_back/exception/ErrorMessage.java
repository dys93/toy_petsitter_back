package com.toy.toy_petsitter_back.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    //기본오류
    //400: 잘못된 요청
    INVALID_PARAMETER(new CustomException(400, 4001, "Invalid `{{}}` Parameter")), //입력 파라미터 오류
    //500: 내부 서버 오류
    UNKNOWN_ERROR(new CustomException(500, 5001, "Unknown Error")), //서버 내 오류
    //404: 찾을 수 없음
    NOT_FOUND(new CustomException(404, 4041, "Not found")), //잘못된 URL 접근
    //403: 금지됨
    UNMATCHED_AUTHORITY(new CustomException(403, 4031, "Unmatched Authority")), //권한 불일치 오류
    //401: 인증필요 //로그인 만료(EXPIRED_TOKEN), 잘못된 접근, 중복로그인 로그아웃(DUPLICATION_LOGIN), 메뉴 접근권한 없음(PERMISSION_DENIED)
    INVALID_TOKEN(new CustomException(401, 4011, "잘못된 접근입니다")), //토큰 오류
    EXPIRED_TOKEN(new CustomException(401, 4012, "로그인이 만료되었습니다")), //만료된 토큰 오류


    //커스텀 오류
    //HTTP 오류코드는 200, 서브코드로 구분
    //유저 관련 오류
    ALREADY_ID(new CustomException(200, 2001, "이미 존재하는 아이디 입니다")), //중복 아이디 오류
    UNMATCHED_ID_PWD(new CustomException(200, 2002, "아이디 혹은 비밀번호를 확인해 주세요")), //아이디나 비밀번호 불일치
    LOCK_USER(new CustomException(200, 2003, "계정이 잠겼습니다. 관리자에게 문의해 주세요")) //비밀번호 오류 횟수 3회 이상 -> 계정잠김



    ;

   private final CustomException exception;

    ErrorMessage(CustomException e) {
        this.exception = e;
    }
}
