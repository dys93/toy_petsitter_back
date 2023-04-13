package com.toy.toy_petsitter_back.controller;

import com.toy.toy_petsitter_back.auth.AuthCheck;
import com.toy.toy_petsitter_back.response.RestResponse;
import com.toy.toy_petsitter_back.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RequestMapping("/api/v1/user")
@RestController
public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입
     * @return
     */
    @AuthCheck(role = AuthCheck.Role.NONE)
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<?> signUp() {
        return new RestResponse().ok().setBody(userService.signUp(
                getParameter("authority"), getParameter("email"), getParameter("password"), getParameter("name"),
                getParameter("phone"), getParameterOrNull("zipCode"), getParameter("address"), getParameterOrNull("addressDetail"),
                getParameterOrNull("petYn"), getParameterOrNull("experience"), getParameterOrNull("exDetail"))
        ).responseEntity();
    }

    /**
     * 로그인
     * @return
     */
    @AuthCheck(role = AuthCheck.Role.NONE)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login() {
        System.out.println(">>>>>>>>>>>>>>>로그인 컨트롤러"+getParameter("id") + getParameter("pwd"));
        return new RestResponse().ok().setBody(userService.login(getParameter("id"), getParameter("pwd"))).responseEntity();
    }

    /**
     * 비밀번호 변경
     * @return
     */
    @AuthCheck(role = AuthCheck.Role.USER)
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword() {
        return new RestResponse().ok().setBody(userService.changePassword(getParameter("pwd"))).responseEntity();
    }

    /**
     * 내 정보 보기
     */
    @AuthCheck(role = AuthCheck.Role.USER)
    @RequestMapping(value = "/myPage", method = RequestMethod.POST)
    public ResponseEntity<?> myPage() {
        System.out.println(">>>>>>>>>>>마이페이지 controller");
        return new RestResponse().ok().setBody(userService.myPage()).responseEntity();
    }

    /**
     * 내 정보 수정
     */
    @AuthCheck(role = AuthCheck.Role.USER)
    @RequestMapping(value = "/saveUserInfo", method = RequestMethod.POST)
    public ResponseEntity<?> saveUserInfo() {
        System.out.println(">>>>>>>>>>>saveUserInfo controller");
        return new RestResponse().ok().setBody(userService.saveUserInfo(getParameter("phone"), getParameterOrNull("zipCode"), getParameter("address"), getParameterOrNull("addressDetail"))).responseEntity();
    }

    /**
     * AccessToken 갱신
     */
    @RequestMapping(value = "/renewToken", method = RequestMethod.POST)
    public ResponseEntity<?> renewToken() {
        System.out.println(">>>>>>>>>>>renewToken controller");
        return new RestResponse().ok().setBody(userService.renewToken(getParameter("refreshToken"))).responseEntity();
    }

    /**
     * 권한 확인
     */
    @AuthCheck(role = AuthCheck.Role.USER)
    @RequestMapping(value = "/checkAuthority", method = RequestMethod.POST)
    public ResponseEntity<?> checkAuthority() {
        System.out.println(">>>>>>>>>>>checkAuthority controller");
        return new RestResponse().ok().setBody(userService.checkAuthority()).responseEntity();
    }

}
