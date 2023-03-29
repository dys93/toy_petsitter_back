package com.toy.toy_petsitter_back.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserRepository {

    HashMap<String, Object> getUserInfo();

    List<HashMap<String, Object>> getUserList();

    String checkId(String id);

    //로그인 할 때 ID랑 PWD (나중에 로그인 시 내려줄 데이터 추가) 정보 져오기
    HashMap<String, Object> getLoginInfoHash(String email);

    //회원가입
    void insertUser(HashMap<String, Object> data);

    //비밀번호 오류 횟수 추가
    void addFailCount(Integer failCount, String email);

    //계정 잠금
    void lockUser(Integer failCount, String email);
}
