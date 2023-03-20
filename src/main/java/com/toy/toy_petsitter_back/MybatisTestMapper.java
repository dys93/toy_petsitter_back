package com.toy.toy_petsitter_back;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MybatisTestMapper {
    List<Map<String, Object>> getTest();
}
