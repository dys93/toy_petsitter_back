package com.toy.toy_petsitter_back.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    //공통오류
    INVALID_PARAMETER(new CustomException(400, 4001, "Invalid `{{}}` Parameter")), //입력 파라미터 오류
    UNKNOWN_ERROR(new CustomException(500, 4002, "Unknown Error")), //서버 내 오류
    NOT_FOUND(new CustomException(400, 4003, "Not found")), //잘못된 URL 접근

    //유저 관련 오류
    UNMATCHED_AUTHORITY(new CustomException(300, 3001, "Invalid Parameter")), //권한 불일치 오류
    ALREADY_ID(new CustomException(300, 3002, "이미 존재하는 아이디 입니다")), //중복 아이디 오류
    INVALID_TOKEN(new CustomException(300, 3003, "잘못된 접근입니다")), //토큰 오류
    EXPIRED_TOKEN(new CustomException(300, 3004, "로그인이 만료되었습니다")) //만료된 토큰 오류

    ;

   private final CustomException exception;

    ErrorMessage(CustomException e) {
        this.exception = e;
    }
}
