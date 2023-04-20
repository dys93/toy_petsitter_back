package com.toy.toy_petsitter_back.repository;

import com.toy.toy_petsitter_back.DTO.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface AdminRepository {

    //회원 리스트 가져오기
    List<HashMap<String, Object>> getUserManageList(HashMap<String, Object> data);

    //회원 리스트 총 수
    int userTotalCount(Integer userSeq);

    //유저 잠금해제
    void unlockUser(Integer userSeq);

    //시터 리스트 가져오기
    List<HashMap<String, Object>> getSitterList(HashMap<String, Object> data);

    //시터 리스트 총 수
    int sitterTotalCount(Integer userSeq);

    //시터 정보 상세 가져오기
    HashMap<String, Object> getSitterDetail(Integer userSeq);

    //시터 승인
    void sitterApproval(Integer userSeq);

    //관리자 예약 리스트 가져오기
    List<HashMap<String, Object>> getReservationManageList(HashMap<String, Object> data);

    //관리자 예약 총 수
    int  reservationManageTotalCount(HashMap<String, Object> data);

    //관리자 환불 처리
    void refundConfirm(Integer reservationSeq);

}
