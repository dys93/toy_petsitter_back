package com.toy.toy_petsitter_back.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.toy.toy_petsitter_back.DTO.Member;
import com.toy.toy_petsitter_back.auth.JwtService;
import com.toy.toy_petsitter_back.config.RecaptchaConfig;
import com.toy.toy_petsitter_back.exception.ErrorMessage;
import com.toy.toy_petsitter_back.repository.UserRepository;
import com.toy.toy_petsitter_back.util.Crypto;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Primary
@Service
public class UserService extends BaseService {

    UserService(UserRepository userRepository, JavaMailSender mailSender, RecaptchaConfig recaptchaConfig) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;

        this.recaptchaConfig = recaptchaConfig;
    }

    private final UserRepository userRepository ;

    private JavaMailSender mailSender;

    final RecaptchaConfig recaptchaConfig;

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
    public HashMap<String, Object> signUp(String authority, String id, String pwd, String name, String phone, String zipCode, String sitterBank, String sitterAccount
                                          ,String address, String address2, String petYn, String experience, String exDetail, String approvalStatus) {

        System.out.println(">>>>>>>>>>>>>>>>>>회원가입 phone"+phone);

        //이미 존재하는 아이디인 경우 오류
        if(userRepository.checkId(id) != null) throw ErrorMessage.ALREADY_ID.getException();

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
        data.put("approvalStatus", approvalStatus);
        data.put("sitterBank", sitterBank);
        data.put("sitterAccount", sitterAccount);
        //회원정보 저장
        userRepository.insertUser(data);

        return new HashMap<>();
    }

    //로그인
    @SneakyThrows
    public HashMap<String, Object> login(String id, String pwd) {
        //회원 데이터 조회(이메일 존재 여부 및 탈퇴한 아이디가 아닐 시 조회됨)
        HashMap<String, Object> resultUser = userRepository.getLoginInfoHash(id);

        //존재하지 않는 아이디면 로그인 실패
        if(resultUser == null) throw ErrorMessage.UNMATCHED_ID_PWD.getException();

        //잠긴 계정 여부 확인
        if(Objects.equals(resultUser.get("user_status").toString(), "R")) throw ErrorMessage.LOCK_USER.getException();

        //비밀번호 오류 횟수
        //int count = (int)resultUser.get("fail_count");

        //비밀번호가 틀렸으면 로그인 실패
        if(!resultUser.get("password").equals(pwd)) {
            //오류 2회 이상
//            if(count >= 2) {
//                //계정 잠금 및 오류 횟수 추가, 오류 내보내기
//                count++;
//                userRepository.lockUser(count, id);
//                throw ErrorMessage.LOCK_USER.getException();
//            }
            //오류 횟수 카운트 + 1, 오류 내보내기
//            count++;
//            userRepository.addFailCount(count, id);
            throw ErrorMessage.UNMATCHED_ID_PWD.getException();
        }

        //아이디 존재 + 비밀번호 일치 => 로그인 : accessToken 생성해서 응답 내려줌 + 중복로그인 여부 확인

        //토큰 생성 및 refreshToken & 생성일자 저장
        HashMap<String, Object> result = new JwtService().createJwt(resultUser.get("user_seq").toString());
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

        //마지막 로그인 일자 update
        userRepository.updateLastLogin(Integer.parseInt(resultUser.get("user_seq").toString()));

        if(resultUser.get("temp_yn").equals("Y")){
            System.out.println(">>>>>>>>>>>>>>>>temp_yn"+resultUser.get("temp_yn"));
            result.put("tempYn", "Y");
        }

        return result;
    }

    //비밀번호 변경
    @SneakyThrows
    public HashMap<String, Object> changePassword(String pwd) {

        //user_seq로 email 가져오기
        String email = userRepository.selectEmail(getUserKey());
        System.out.println(">>>>>>>>>>>>>>>>>>>changePassword_selectEmail(): "+email);

        //새로운 비밀번호
        String newPwd = Crypto.encodeSHA256(pwd);
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
            throw ErrorMessage.INVALID_TOKEN.getException();
        }
        //일치하는 경우 => access토큰, refresh토큰 갱신 및 응답
        HashMap<String, Object> userResult = userRepository.findUserKey(userKey);
        System.out.println(">>>>>>>>>>>>>>>>>>>user_seq"+userResult.get("user_seq").toString());

        //토큰 생성 및 refreshToken & issuedDate 저장
        HashMap<String, Object> result = new JwtService().createJwt(userResult.get("user_seq").toString());
        System.out.println(">>>>>>>>>>>토큰 생성 및 refreshToken 저장_refreshToken"+result.get("refreshToken"));
        userRepository.updateToken(userKey, result.get("refreshToken").toString(), result.get("issuedDate").toString());

        return result;
    }

    //권한 체크
    public HashMap<String, Object> checkAuthority() {

        System.out.println(">>>>>>>>>>checkAuthority() UserService");

        //토큰에서 user_key 가져와서 권한 확인 후 내림
        String authority = userRepository.selectAuthority(getUserKey());
        System.out.println(">>>>>>>>>>권한 체크_authority"+authority);

        return  new HashMap<>() {{
           put("authority", authority);
        }};
    }

    //이메일 발송
    @Transactional
    @SneakyThrows
    public HashMap<String, Object> sendEmail(String email) {

        System.out.println(">>>>>>>>>>sendEmail() UserService");

        //DB에 있는 이메일 주소인지 체크
        String emailId = userRepository.checkEmail(email);
        if(emailId == null) {
            throw ErrorMessage.NONE_EXIST.getException();
        }

        //임시 비밀번호 생성
        String str = getTempPassword();

        //메일발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("팻플래닛 임시 비밀번호 이메일 입니다.\n");
        message.setText("안녕하세요, 임시비밀번호 안내 메일입니다.\n" + "회원님의 임시 비밀번호는 " + str + " 입니다.\n"+ "로그인 후에 비밀번호를 변경해주세요.");
        message.setFrom("lyj3637@naver.com");
        message.setReplyTo("lyj3637@naver.com");
        System.out.println("메일 발송:"+message);
        mailSender.send(message);

        //메일 성공적으로 발송 시 해당 계정의 비밀번호 변경
        String newStr = Crypto.encodeSHA256(str); //암호화
        userRepository.updateTempPassword(newStr, email);

        //lock 해제
        userRepository.unlock(email);

        return  new HashMap<>();
    }

    //임시 비밀번호 생성
    public String getTempPassword() {
        char[] charSet = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        //문자 배열 길이의 값을 랜덤으로 8개를 뽑아 구분 작성
        int idx = 0;
        for(int i =0; i<8; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }

        System.out.println("임시 비밀번호"+str);

        return str;
    }

    //회원 탈퇴 withdrawal
    @Transactional
    @SneakyThrows
    public HashMap<String, Object> withdrawal() {

        System.out.println(">>>>>>>>>>withdrawal() UserService");

        //권한 확인
        String authority = userRepository.selectAuthority(getUserKey());

        //펫시터의 경우
        if(authority.equals("P")) {
            if(!userRepository.checkWithdrawal(getUserKey()).equals("0")){
                //탈퇴 불가 에러
                throw ErrorMessage.CANNOT_WITHDRAWAL.getException();
            }
        } else {
            if(!userRepository.checkUserWithdrawal(getUserKey()).equals("0")) {
                throw ErrorMessage.CANNOT_WITHDRAWAL.getException();
            }
        }
        userRepository.withdrawal(getUserKey());

        return  new HashMap<>();
    }

    //reCaptcha
    @SneakyThrows
    public HashMap<String, Object> verifyRecaptcha(String recaptcha) {
        System.out.println(">>>>>>>>>>>>>>recatcha Service");

        String secretKey = recaptchaConfig.getSecret();
        String url = recaptchaConfig.getUrl();

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        String postParams = "secret=" + secretKey + "&response=" + recaptcha;
        System.out.println(">>>>>>>>>>>>>>>>postParams"+postParams);
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        System.out.println(">>>>>>>>>>>>>>>>getBoolean success"+jsonObject.getBoolean("success"));

        return new HashMap<>() {{
            put("recaptcha", jsonObject.getBoolean("success"));
            //put("recaptcha", "false");
        }};

    }

}
