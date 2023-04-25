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

    //게시글 삭제(상태 변경)
    void deleteMyPost(Integer userSeq);

    //게시글 목록 가져오기
    List<HashMap<String, Object>> getPostList(HashMap<String, Object> data);
//    List<HashMap<String, Object>> getPostList(Criteria criteria);

    //게시글 총 갯수
    int totalCount(HashMap<String, Object> data);

    //리뷰 총 갯수
    int reviewTotalCount(Integer petsitSeq);

    //게시글 작성 상태 변경
    void updatePostStatus(String postYn, Integer userSeq);

    //게시글 상세(예약 페이지) 불러오기
    HashMap<String, Object> getPostDetail(Integer petsitSeq);

    //리뷰 불러오기
    List<HashMap<String, Object>> getReview(HashMap<String, Object> data);

    //예약 요청 저장
    void insertReservation(HashMap<String, Object> data);

    //시터 seq 가져오기
    int selectSitterSeq(Integer petSitSeq);

    //예약 리스트 가져오기
    List<HashMap<String, Object>> getReservationList(HashMap<String, Object> data);

    //예약 내역 가져오기(유저 정보 포함)
    HashMap<String, Object> getReservationInfo(Integer reservationSeq, Integer userSeq);

    //예약 내역 가져오기
    HashMap<String, Object> getReservationInfoOnly(Integer reservationSeq);

    //총 예약 수
    int totalReservationCount(Integer userSeq);

    //결제하기
    void insertPayment(HashMap<String, Object> data);

    //예약 상태 변경
    void changeReservationStatus(String reservationStatus, Integer reservationSeq);

    //결제 환불
    void refund(Integer reservationSeq);

    //리뷰 작성
    void review(HashMap<String, Object> data);

    //시터 예약 리스트 가져오기
    List<HashMap<String, Object>> getReservationManageList(HashMap<String, Object> data);

    //시터 총 예약 수
    int totalReservationManageCount(Integer userSeq);
}
