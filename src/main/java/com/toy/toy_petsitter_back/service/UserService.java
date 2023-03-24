package com.toy.toy_petsitter_back.service;

import com.toy.toy_petsitter_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

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

}
