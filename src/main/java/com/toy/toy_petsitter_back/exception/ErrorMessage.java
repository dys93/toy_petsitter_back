package com.toy.toy_petsitter_back.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    //공통오류
    INVALID_PARAMETER(new CustomException(400, 4001, "Invalid Parameter")), //입력 파라미터 오류
    UNKNOWN_ERROR(new CustomException(500, 4002, "Unknown Error")), //서버 내 오류
    NOT_FOUND(new CustomException(400, 4003, "Not found")), //잘못된 URL 접근

    //권한오류
    UNMATCHED_AUTHORITY(new CustomException(300, 3001, "Invalid Parameter")) //권한 불일치 오류

    ;

   private final CustomException exception;

    ErrorMessage(CustomException e) {
        this.exception = e;
    }
}
