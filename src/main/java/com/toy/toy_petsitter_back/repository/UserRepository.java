package com.toy.toy_petsitter_back.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserRepository {

    //내정보 가져오기
    HashMap<String, Object> getUserInfo(Integer userKey);
    //내정보 수정
    void updateUserInfo(HashMap<String, Object> data);

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

    //userKey 존재여부 확인
    HashMap<String, Object> findUserKey(Integer userSeq);

    //비밀번호 변경
    void changePassword(String password, String email);

    //Refresh 토큰 생성 저장
    void insertToken(Integer userKey, String refreshToken, String issuedDate);

    //Refresh 토큰 갱신 저장
    void updateToken(Integer userKey, String refreshToken, String issuedDate);

    //Refresh 토큰 가져오기
    String selectToken(Integer userKey);

    //issuedDate 가져오기
    String selectIssuedDate(Integer userSeq);

    //권한 가져오기
    String selectAuthority(Integer userSeq);

    //이메일 가져오기
    String selectEmail(Integer userSeq);

}
