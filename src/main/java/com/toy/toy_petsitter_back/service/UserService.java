package com.toy.toy_petsitter_back.service;

import com.toy.toy_petsitter_back.DTO.Member;
import com.toy.toy_petsitter_back.auth.JwtService;
import com.toy.toy_petsitter_back.exception.ErrorMessage;
import com.toy.toy_petsitter_back.repository.UserRepository;
import com.toy.toy_petsitter_back.util.Crypto;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Primary
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public HashMap<String, Object> getUserInfo() {
        return userRepository.getUserInfo();
    }

    @Transactional
    public HashMap<String, Object> getUserList() {
        List<HashMap<String,Object>> list = userRepository.getUserList();

        return new HashMap<>() {{
           put("userInfo", list);
        }};
    }

    //회원가입
    @Transactional
    @SneakyThrows
    public HashMap<String, Object> signUp(String authority, String id, String pwd, String name, String phone, String zipCode,
                                          String address, String address2, String petYn, String experience, String exDetail) {

        //이미 존재하는 아이디인 경우 오류
        if(userRepository.checkId(id) != null) throw ErrorMessage.ALREADY_ID.getException();

        //비밀번호 암호화
        String encodedPwd = Crypto.encodeSHA256(pwd + id.substring(0,4));
        //String userKey = Crypto.encodeSHA128(System.currentTimeMillis() + System.nanoTime() + id);

        //Service로 넘길 data
        HashMap<String, Object> data = new HashMap<>();
        data.put("authority", authority);
        data.put("email", id);
        data.put("password", encodedPwd);
        data.put("name", name);
        data.put("phone", phone);
        data.put("zipCode", zipCode);
        data.put("address1", address);
        data.put("address2", address2);
        data.put("petExperience", petYn);
        data.put("sitterExperience", experience);
        data.put("experienceDetail", exDetail);

        //회원정보 저장
        userRepository.insertUser(data);

        return new HashMap<>();
    }

    //로그인

    @SneakyThrows
    public HashMap<String, Object> login(String id, String pwd) {

        //회원 데이터 조회
        HashMap<String, Object> resultUser = userRepository.getLoginInfoHash(id);

        //존재하지 않는 아이디면 로그인 실패
        if(resultUser == null) throw ErrorMessage.UNMATCHED_ID_PWD.getException();

        //잠긴 계정 여부 확인
        if(Objects.equals(resultUser.get("user_status").toString(), "R")) throw ErrorMessage.LOCK_USER.getException();


        //비밀번호 오류 횟수
        int count = (int)resultUser.get("fail_count");

        //비밀번호가 틀렸으면 로그인 실패
        if(!resultUser.get("password").equals(Crypto.encodeSHA256(pwd +id.substring(0,4)))) {
            //오류 2회 이상
            if(count >= 2) {
                //계정 잠금 및 오류 횟수 추가, 오류 내보내기
                count++;
                userRepository.lockUser(count, id);
                throw ErrorMessage.LOCK_USER.getException();
            }

            //오류 횟수 카운트 + 1, 오류 내보내기
            count++;
            userRepository.addFailCount(count, id);
            throw ErrorMessage.UNMATCHED_ID_PWD.getException();
        }

        //아이디 존재 + 비밀번호 일치 => 로그인 : accessToken 생성해서 같이 내려줌
        Member member = new Member();
        member.setUserKey(resultUser.get("user_seq").toString());

        HashMap<String, Object> result = new HashMap<>();
        result.put("accessToken", new JwtService().createJwt(member));

        return result;
    }

}
