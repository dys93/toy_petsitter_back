package com.toy.toy_petsitter_back.repository;

import com.toy.toy_petsitter_back.DTO.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PetSitRepository {

    //내 작성글 저장
    void insertMyPost(HashMap<String, Object> data);

    //내 작성글 불러오기
    HashMap<String, Object> getMyPost(Integer userSeq);

    //내 작성글 수정
    void updateMyPost(HashMap<String, Object> data);

    //게시글 목록 가져오기
    List<HashMap<String, Object>> getPostList(Criteria criteria);

    //게시글 총 갯수
    int totalCount();

    //게시글 작성 상태 변경
    void updatePostStatus(String postYn, Integer userSeq);

    //게시글 상세(예약 페이지) 불러오기
    HashMap<String, Object> getPostDetail(Integer petsitSeq);

    //게시글 삭제(상태 변경)
     void deleteMyPost(Integer userSeq);

}
