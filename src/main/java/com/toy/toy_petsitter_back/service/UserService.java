package com.toy.toy_petsitter_back.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.toy.toy_petsitter_back.DTO.Criteria;
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
public class UserService extends BaseService {

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository ;

    @Transactional
    public HashMap<String, Object> getUserList() {
        List<HashMap<String,Object>> list = userRepository.getUserList();

        return new HashMap<>() {{
           put("userInfo", list);
        }};
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //회원가입
    @Transactional
    @SneakyThrows
    public HashMap<String, Object> signUp(String authority, String id, String pwd, String name, String phone, String zipCode,
                                          String address, String address2, String petYn, String experience, String exDetail) {

        //이미 존재하는 아이디인 경우 오류
        if(userRepository.checkId(id) != null) throw ErrorMessage.ALREADY_ID.getException();

        //비밀번호 암호화 -> 프론트에서 암호화
        //String encodedPwd = Crypto.encodeSHA256(pwd + id.substring(0,4));
        //String userKey = Crypto.encodeSHA128(System.currentTimeMillis() + System.nanoTime() + id);

        //Service로 넘길 data
        HashMap<String, Object> data = new HashMap<>();
        data.put("authority", authority);
        data.put("email", id);
        data.put("password", pwd);
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
        if(!resultUser.get("password").equals(pwd)) {
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

        //아이디 존재 + 비밀번호 일치 => 로그인 : accessToken 생성해서 응답 내려줌 + 중복로그인 여부 확인
        Member member = new Member();
        member.setUserKey(resultUser.get("user_seq").toString());

        //토큰 생성 및 refreshToken & 생성일자 저장
        HashMap<String, Object> result = new JwtService().createJwt(member);
        result.get("refreshToken");
        result.get("issuedDate");
        System.out.println(">>>>>>>>>>>토큰 생성 및 refreshToken 저장_refreshToken"+result.get("refreshToken"));
        System.out.println(">>>>>>>>>>>토큰 생성 및 refreshToken 저장_issuedDate"+result.get("issuedDate"));

        //최초 로그인이 아닌 이상 refreshToken은 insert가 아닌 update
        if(userRepository.selectToken(Integer.parseInt(resultUser.get("user_seq").toString())) == null) {
            userRepository.insertToken(Integer.parseInt(resultUser.get("user_seq").toString()), result.get("refreshToken").toString(), result.get("issuedDate").toString());
            System.out.println(">>>>>>>>>>>refreshToken & issuedDate insert");
        } else {
            userRepository.updateToken(Integer.parseInt(resultUser.get("user_seq").toString()), result.get("refreshToken").toString(), result.get("issuedDate").toString());
            System.out.println(">>>>>>>>>>>refreshToken & issuedDate update");
        }

        return result;
    }

    //비밀번호 변경
    @SneakyThrows
    public HashMap<String, Object> changePassword(String pwd) {

        //user_seq로 email 가져오기
        String email = userRepository.selectEmail(getUserKey());
        System.out.println(">>>>>>>>>>>>>>>>>>>changePassword_selectEmail(): "+email);

        //메일 넣어서 변경
        //새로운 비밀번호
        String newPwd = Crypto.encodeSHA256(pwd + email.substring(0, 4));
        System.out.println(">>>>>>>>>>>>>>>>>changePassword_newPwd"+newPwd);

        //비밀번호 변경
        userRepository.changePassword(newPwd, email);

        return new HashMap<>();
    }

    //마이 페이지
    @SneakyThrows
    public HashMap<String, Object> myPage() {
        System.out.println(">>>>>>>>>>>마이페이지 Service");
        return userRepository.getUserInfo(getUserKey());
    }

    //마이 페이지 수정
    @SneakyThrows
    public HashMap<String, Object> saveUserInfo(String phone, String zipCode, String address, String addressDetail) {
        System.out.println(">>>>>>>>>>>마이페이지 수정 Service");

        HashMap<String, Object> data = new HashMap<>();
        data.put("phone", phone);
        data.put("zipCode", zipCode);
        data.put("address", address);
        data.put("addressDetail", addressDetail);
        data.put("userKey", getUserKey());

        System.out.println(">>>>>>>>"+data.put("phone", phone));
        System.out.println(">>>>>>>>"+data.put("zipCode", zipCode));
        System.out.println(">>>>>>>>"+data.put("address", address));
        System.out.println(">>>>>>>>"+data.put("addressDetail", addressDetail));
        System.out.println(">>>>>>>>"+data.put("userKey", getUserKey()));

        userRepository.updateUserInfo(data);
        return new HashMap<>();
    }


    //AccessToken & RefreshToken 갱신
    @SneakyThrows
    public HashMap<String, Object> renewToken(String refreshToken){
        System.out.println(">>>>>>>>>>>renewToken Service");

        //refreshToken 디코딩 //유효기간 지났을 시 재로그인 에러 내림
        DecodedJWT decodeJWT = new JwtService().getDecodedJwtWithTokenR(refreshToken);

        //DB에서 R토큰 조회해와서
        Integer userKey = Integer.parseInt(decodeJWT.getClaim("userKey").asString());
        System.out.println(">>>>>>>>>>>renewToken_userKey: "+userKey);
        String refreshTokenDb = userRepository.selectToken(userKey);
        //받은 R토큰이랑 DB R토큰이랑 비교
        if(!refreshToken.equals(refreshTokenDb)){
            //일치하지 않는 경우 => 에러 응답
            System.out.println(">>>>>>>>>>>>>>>>>>>R토큰 끼리 일치하지 않는 경우 에러 응답");
            ErrorMessage.INVALID_TOKEN.getException();
        }
        //일치하는 경우 => access토큰, refresh토큰 갱신 및 응답
        Member member = new Member();
        HashMap<String, Object> userResult = userRepository.findUserKey(userKey);
        System.out.println(">>>>>>>>>>>>>>>>>>>user_seq"+userResult.get("user_seq").toString());
        System.out.println(">>>>>>>>>>>>>>>>>>>email"+userResult.get("email").toString());
        member.setUserKey(userResult.get("user_seq").toString());
        member.setEmail(userResult.get("email").toString());


        //토큰 생성 및 refreshToken & issuedDate 저장
        HashMap<String, Object> result = new JwtService().createJwt(member);
        System.out.println(">>>>>>>>>>>토큰 생성 및 refreshToken 저장_refreshToken"+result.get("refreshToken"));
        userRepository.updateToken(userKey, result.get("refreshToken").toString(), result.get("issuedDate").toString());

        return result;
    }

    //권한 체크
    public HashMap<String, Object> checkAuthority() {

        System.out.println(">>>>>>>>>>checkAuthority() UserService");

        //토큰에서 user_key 가져와서 권한 확인 후 내림
        System.out.println(">>>>>>>>>>권한 체크_getUserKey()"+getUserKey());
        String authority = userRepository.selectAuthority(getUserKey());
        System.out.println(">>>>>>>>>>권한 체크_authority"+authority);

        return  new HashMap<>() {{
           put("authority", authority);
        }};
    }
}
