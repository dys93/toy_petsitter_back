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
}
