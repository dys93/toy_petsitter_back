package com.toy.toy_petsitter_back.controller;

import com.toy.toy_petsitter_back.DTO.Criteria;
import com.toy.toy_petsitter_back.auth.AuthCheck;
import com.toy.toy_petsitter_back.response.RestResponse;
import com.toy.toy_petsitter_back.service.AdminService;
import com.toy.toy_petsitter_back.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin")
@RestController
public class DashboardController extends BaseController {

    private final DashboardService dashboardService;
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * 매칭 관련 건수 가져오기
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/selectMatchingCnt", method = RequestMethod.GET)
    public ResponseEntity<?> selectMatchingCnt() {
        System.out.println(">>>>>>>>>>매칭 관련 건수 가져오기 컨트롤러");

        return new RestResponse().ok().setBody(dashboardService.selectMatchingCnt()).responseEntity();
    }

    /**
     * 결제 금액 가져오기
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/getPaymentAmount", method = RequestMethod.GET)
    public ResponseEntity<?> selectPaymentAmount() {
        System.out.println(">>>>>>>>>>결제 금액 가져오기 컨트롤러");

        return new RestResponse().ok().setBody(dashboardService.selectPaymentAmount()).responseEntity();
    }

    /**
     * 회원수 가져오기
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/getUserCount", method = RequestMethod.GET)
    public ResponseEntity<?> selectUserCount() {
        System.out.println(">>>>>>>>>>회원수 가져오기 컨트롤러");

        return new RestResponse().ok().setBody(dashboardService.selectUserCount()).responseEntity();
    }

}
