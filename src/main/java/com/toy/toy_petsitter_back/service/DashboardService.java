package com.toy.toy_petsitter_back.service;

import com.toy.toy_petsitter_back.repository.DashboardRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DashboardService extends  BaseService{


    DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    private final DashboardRepository dashboardRepository;


    //매칭(예약) 총 수 가져오기
    public HashMap<String, Object> selectMatchingCnt() {
        System.out.println(">>>>>>>>매칭(예약) 총 수 가져오기 Service");

        return new HashMap<>() {{
            put("selectMatchingCnt", dashboardRepository.selectMatchingCnt());
            put("selectAcceptCnt", dashboardRepository.selectAcceptCnt());
            put("selectRejectCnt", dashboardRepository.selectRejectCnt());
            put("selectPaymentCnt", dashboardRepository.selectPaymentCnt());
        }};
    }

    //결제 금액 가져오기
    public HashMap<String, Object> selectPaymentAmount() {
        System.out.println(">>>>>>>>결제 금액 가져오기 Service");

        return new HashMap<>() {{
            put("selectAmountToday", dashboardRepository.selectAmountToday());
            put("selectAmountTodayOne", dashboardRepository.selectAmountTodayOne());
            put("selectAmountTodayTwo", dashboardRepository.selectAmountTodayTwo());
            put("selectAmountTodayThree", dashboardRepository.selectAmountTodayThree());
            put("selectAmountTodayFour", dashboardRepository.selectAmountTodayFour());
        }};
    }

    //회원 수 가져오기
    public HashMap<String, Object> selectUserCount() {
        System.out.println(">>>>>>>>회원 수 가져오기 Service");

        return new HashMap<>() {{
            put("lastMonthUserAll", dashboardRepository.lastMonthUserAll());
            put("thisMonthUserAll", dashboardRepository.thisMonthUserAll());

            put("lastMonthUserOnly", dashboardRepository.lastMonthUserOnly());
            put("thisMonthUserOnly", dashboardRepository.thisMonthUserOnly());

            put("lastMonthSitter", (Integer.parseInt(dashboardRepository.lastMonthUserAll()) - Integer.parseInt(dashboardRepository.lastMonthUserOnly())));
            put("thisMonthSitter", (Integer.parseInt(dashboardRepository.thisMonthUserAll()) - Integer.parseInt(dashboardRepository.thisMonthUserOnly())));


        }};
    }

}
