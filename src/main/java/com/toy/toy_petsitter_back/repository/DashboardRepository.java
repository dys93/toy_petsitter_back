package com.toy.toy_petsitter_back.repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface DashboardRepository {

    //매칭 총 수
    int  selectMatchingCnt();

    //수락 수
    int  selectAcceptCnt();

    //거절 수
    int  selectRejectCnt();

    //결제 수
    int  selectPaymentCnt();

    //결제 금액
    String selectAmountToday();
    String selectAmountTodayOne();
    String selectAmountTodayTwo();
    String selectAmountTodayThree();
    String selectAmountTodayFour();

    String lastMonthUserAll();

    String thisMonthUserAll();

    String lastMonthUserOnly();

    String thisMonthUserOnly();


}
