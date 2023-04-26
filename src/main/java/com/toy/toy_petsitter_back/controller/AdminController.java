package com.toy.toy_petsitter_back.controller;

import com.toy.toy_petsitter_back.DTO.Criteria;
import com.toy.toy_petsitter_back.auth.AuthCheck;
import com.toy.toy_petsitter_back.repository.UserRepository;
import com.toy.toy_petsitter_back.response.RestResponse;
import com.toy.toy_petsitter_back.service.AdminService;
import com.toy.toy_petsitter_back.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin")
@RestController
public class AdminController extends BaseController {

    private final AdminService adminService;
    private final UserService userService;
    private final UserRepository userRepository;

    public AdminController(AdminService adminService, UserService userService, UserRepository userRepository) {
        this.adminService = adminService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * 로그인
     */
    @AuthCheck(role = AuthCheck.Role.NONE)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login() {
        System.out.println(">>>>>>>>>>관리자 로그인 컨트롤러");
        return new RestResponse().ok().setBody(adminService.login(getParameter("id"), getParameter("pwd"))).responseEntity();
    }

    /**
     * 유저 리스트 가져오기
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/getUserManageList", method = RequestMethod.GET)
    public ResponseEntity<?> getUserManageList() {
        System.out.println(">>>>>>>>>>유저 리스트 가져오기 컨트롤러");

        Criteria criteria = new Criteria();
        criteria.setPerPageNum(10);
        criteria.setPage(Integer.parseInt(getParameter("pageNum"))); //현재 페이지

        return new RestResponse().ok().setBody(adminService.getUserManageList(criteria)).responseEntity();
    }

    /**
     * 유저 잠금 해제
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/unlockUser", method = RequestMethod.PUT)
    public ResponseEntity<?> unlockUser() {
        System.out.println(">>>>>>>>>>유저 잠금 해제 컨트롤러");

        System.out.println(">>>>>>>>>>>>>>>>>>>userSeq"+getParameter("userSeq"));

        return new RestResponse().ok().setBody(adminService.unlockUser(Integer.parseInt(getParameter("userSeq")))).responseEntity();
    }

    /**
     * 시터 리스트 가져오기
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/getSitterList", method = RequestMethod.GET)
    public ResponseEntity<?> getSitterList() {
        System.out.println(">>>>>>>>>>시터 리스트 가져오기 컨트롤러");

        Criteria criteria = new Criteria();
        criteria.setPerPageNum(10);
        criteria.setPage(Integer.parseInt(getParameter("pageNum"))); //현재 페이지

        return new RestResponse().ok().setBody(adminService.getSitterList(criteria)).responseEntity();
    }

    /**
     * 시터 정보 상세 가져오기
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/getSitterDetail", method = RequestMethod.GET)
    public ResponseEntity<?> getSitterDetail() {
        System.out.println(">>>>>>>>>>시터 정보 상세 가져오기 컨트롤러");

        return new RestResponse().ok().setBody(adminService.getSitterDetail(Integer.parseInt(getParameter("userSeq")))).responseEntity();
    }

    /**
     * 시터 승인
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/sitterApproval", method = RequestMethod.GET)
    public ResponseEntity<?> sitterApproval() {
        System.out.println(">>>>>>>>>>시터 승인 컨트롤러");

        return new RestResponse().ok().setBody(adminService.sitterApproval(Integer.parseInt(getParameter("userSeq")))).responseEntity();
    }

    /**
     * 관리자 예약 리스트 가져오기
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/getReservationManageList", method = RequestMethod.GET)
    public ResponseEntity<?> getReservationManageList() {
        System.out.println(">>>>>>>>>>관리자 예약 리스트 가져오기 컨트롤러");

        Criteria criteria = new Criteria();
        criteria.setPerPageNum(10);
        criteria.setPage(Integer.parseInt(getParameter("pageNum"))); //현재 페이지

        return new RestResponse().ok().setBody(adminService.getReservationManageList(criteria, getParameter("orderBy"),
                getParameterOrNull("type"), getParameterOrNull("search"))).responseEntity();
    }

    /**
     * 관리자 예약 취소
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/reservationCancel", method = RequestMethod.PUT)
    public ResponseEntity<?> reservationCancel() {
        System.out.println(">>>>>>>>>>관리자 예약 취소 컨트롤러");

        return new RestResponse().ok().setBody(adminService.reservationCancel(Integer.parseInt(getParameter("reservationSeq")))).responseEntity();
    }

    /**
     * 관리자 환불 처리
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/refundConfirm", method = RequestMethod.PUT)
    public ResponseEntity<?> refundConfirm() {
        System.out.println(">>>>>>>>>>관리자 환불 처리 컨트롤러");

        return new RestResponse().ok().setBody(adminService.refundConfirm(Integer.parseInt(getParameter("reservationSeq")))).responseEntity();
    }

    /**
     * 임시 비밀번호 발송
     */
    @AuthCheck(role = AuthCheck.Role.ADMIN)
    @RequestMapping(value = "/sendEmail", method = RequestMethod.PUT)
    public ResponseEntity<?> sendEmail() {
        System.out.println(">>>>>>>>>>임시 비밀번호 발송 컨트롤러");

        System.out.println(">>>>>>>>>>>>>>>>>>>userSeq"+getParameter("userSeq"));

        //이메일 주소 가져오기
        String email = userRepository.selectEmail(Integer.parseInt(getParameter("userSeq")));

        return new RestResponse().ok().setBody(userService.sendEmail(email)).responseEntity();
    }
}
