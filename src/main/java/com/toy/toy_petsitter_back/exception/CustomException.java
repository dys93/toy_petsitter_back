package com.toy.toy_petsitter_back.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends Exception{

    private int code;
    private int subCode;
    private String msg;

    protected CustomException(int code, int subCode, String msg) {
        this.code = code;
        this.subCode = subCode;
        this.msg = msg;
    }


}
