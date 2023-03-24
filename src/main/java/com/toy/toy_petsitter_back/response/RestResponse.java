package com.toy.toy_petsitter_back.response;


import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class RestResponse {

    HttpHeaders headers = new HttpHeaders();

    @Expose private Map<String, Object> data = new HashMap<>();
    @Expose private int code = 0;

    @Expose private int subCode = 0;
    @Expose private String error = "";

    public RestResponse() {
        restResponse();
    }

    //로그 테스트
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public RestResponse ok() {
        RestResponse instance = this;
        instance.code = HttpStatus.OK.value();
        instance.error = "Success";
        return instance;
    }

    public RestResponse setBody(HashMap<String, Object> body) {
        data = body;
        return this;
    }

    public ResponseEntity<Object> responseEntity() {
        logger.info(">>>>> Response : " + toJasonString());
        return new ResponseEntity<>(toJasonString(), headers, HttpStatus.valueOf(code));
    }


    private void restResponse() {
        addHeader("Content-Type", "application/json");
        addHeader("charset", "utf-8");
    }

    private void addHeader(String name, String value) {
        headers.set(name, value);
    }

    public String toJasonString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
    }
}
