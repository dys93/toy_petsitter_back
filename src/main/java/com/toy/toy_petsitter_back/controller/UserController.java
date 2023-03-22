package com.toy.toy_petsitter_back.controller;

import com.toy.toy_petsitter_back.response.RestResponse;
import com.toy.toy_petsitter_back.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/userInfo")
    public ResponseEntity<?> userInfo() {
        return new RestResponse().ok().setBody(userService.getUserInfo()).responseEntity();
    }

}
