package com.toy.toy_petsitter_back.service;

import com.toy.toy_petsitter_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Primary
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public HashMap<String, Object> getUserInfo() {
        return userRepository.getUserInfo();
    }

}
