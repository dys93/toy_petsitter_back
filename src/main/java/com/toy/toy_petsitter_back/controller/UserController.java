package com.toy.toy_petsitter_back.controller;

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

//    @RequestMapping(value = "/userInfo")
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public ResponseEntity<?> userInfo() {
        return new RestResponse().ok().setBody(userService.getUserInfo()).responseEntity();
    }

    @RequestMapping(value = "/userList")
    public ResponseEntity<?> userList() {
        return new RestResponse().ok().setBody(userService.getUserList()).responseEntity();
    }

    //회원가입
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<?> signUp() {
        return new RestResponse().ok().setBody(userService.signUp(
                getParameter("authority"), getParameter("email"), getParameter("password"), getParameter("name"),
                getParameter("phone"), getParameterOrNull("zipCode"), getParameter("address"), getParameterOrNull("addressDetail"),
                getParameterOrNull("petYn"), getParameterOrNull("experience"), getParameterOrNull("exDetail"))
        ).responseEntity();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login() {
        System.out.println(">>>>>>>>>>로그인ID"+getParameter("id"));
        System.out.println(">>>>>>>>>>로그인PWD"+getParameter("pwd"));
        return new RestResponse().ok().setBody(userService.login(getParameter("id"), getParameter("pwd"))).responseEntity();
    }

}
