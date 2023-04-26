package com.toy.toy_petsitter_back.service;

import com.toy.toy_petsitter_back.DTO.Criteria;
import com.toy.toy_petsitter_back.DTO.Pagination;
import com.toy.toy_petsitter_back.auth.JwtService;
import com.toy.toy_petsitter_back.exception.ErrorMessage;
import com.toy.toy_petsitter_back.repository.AdminRepository;
import com.toy.toy_petsitter_back.repository.PetSitRepository;
import com.toy.toy_petsitter_back.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
public class AdminService extends  BaseService{


    AdminService(UserRepository userRepository, AdminRepository adminRepository, PetSitRepository petSitRepository, PetSitService petSitService) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.petSitRepository = petSitRepository;
        this.petSitService = petSitService;
    }

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    private final PetSitRepository petSitRepository;

    private final PetSitService petSitService;

    //로그인
    @SneakyThrows
    public HashMap<String, Object> login(String id, String pwd) {
        //회원 데이터 조회
        HashMap<String, Object> resultUser = userRepository.getLoginInfoHash(id);

        //존재하지 않는 아이디면 로그인 실패
        if(resultUser == null) throw ErrorMessage.UNMATCHED_ID_PWD.getException();

        //비밀번호가 틀렸으면 로그인 실패
        if(!resultUser.get("password").equals(pwd)) {
            throw ErrorMessage.UNMATCHED_ID_PWD.getException();
        }

        //아이디 존재 + 비밀번호 일치 => 로그인 : accessToken 생성해서 응답 내려줌 + 중복로그인 여부 확인

        //토큰 생성 및 refreshToken 저장
        HashMap<String, Object> result = new JwtService().createJwt(resultUser.get("user_seq").toString());
        result.get("refreshToken");
        System.out.println(">>>>>>>>>>>토큰 생성 및 refreshToken 저장_refreshToken"+result.get("refreshToken"));

        //최초 로그인이 아닌 이상 refreshToken은 insert가 아닌 update
        if(userRepository.selectToken(Integer.parseInt(resultUser.get("user_seq").toString())) == null) {
            userRepository.insertToken(Integer.parseInt(resultUser.get("user_seq").toString()), result.get("refreshToken").toString(), result.get("issuedDate").toString());
        } else {
            userRepository.updateToken(Integer.parseInt(resultUser.get("user_seq").toString()), result.get("refreshToken").toString(), result.get("issuedDate").toString());
        }

        //마지막 로그인 일자 update
        userRepository.updateLastLogin(Integer.parseInt(resultUser.get("user_seq").toString()));

        return result;
    }

    //회원 리스트 가져오기
    public HashMap<String, Object> getUserManageList(Criteria criteria) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>회원 리스트 가져오기 Service");
        Pagination pagination = new Pagination();
        pagination.setCriteria(criteria);
        pagination.setTotalCount(getUserTotalCount());

        HashMap<String, Object> data = new HashMap<>();
        data.put("pageStart", criteria.getPageStart());
        System.out.println(">>>>>>>>>>>>>>>>criteria.getPageStart()"+criteria.getPageStart());
        data.put("perPageNum", criteria.getPerPageNum());
        System.out.println(">>>>>>>>>>>>>>>>criteria.getPerPageNum()"+criteria.getPerPageNum());

        return new HashMap<>() {{
            put("userList", adminRepository.getUserManageList(data));
            put("page", criteria.getPage());
            put("totalCount", pagination.getTotalCount());
            put("startPage", pagination.getStartPage());
            put("endPage", pagination.getEndPage());
        }};
    }

    //회원 총 수 가져오기
    public Integer getUserTotalCount() {
        System.out.println(">>>>>>>>회원 총 수 가져오기 Service");
        return adminRepository.userTotalCount(getUserKey());
    }

    //유저 잠금 해제
    @Transactional
    public HashMap<String, Object> unlockUser(Integer userSeq) {
        System.out.println(">>>>>>>>유저 잠금 해제 Service");

        //잠금 해제 및 로그인 실패 횟수 0 변경
        adminRepository.unlockUser(userSeq);

        return new HashMap<>();
    }

    //시터 리스트 가져오기
    public HashMap<String, Object> getSitterList(Criteria criteria) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>시터 리스트 가져오기 Service");
        Pagination pagination = new Pagination();
        pagination.setCriteria(criteria);
        pagination.setTotalCount(getSitterTotalCount());

        HashMap<String, Object> data = new HashMap<>();
        data.put("pageStart", criteria.getPageStart());
        System.out.println(">>>>>>>>>>>>>>>>criteria.getPageStart()"+criteria.getPageStart());
        data.put("perPageNum", criteria.getPerPageNum());
        System.out.println(">>>>>>>>>>>>>>>>criteria.getPerPageNum()"+criteria.getPerPageNum());

        return new HashMap<>() {{
            put("sitterList", adminRepository.getSitterList(data));
            put("page", criteria.getPage());
            put("totalCount", pagination.getTotalCount());
            put("startPage", pagination.getStartPage());
            put("endPage", pagination.getEndPage());
        }};
    }

    //시터 총 수 가져오기
    public Integer getSitterTotalCount() {
        System.out.println(">>>>>>>>시터 총 수 가져오기 Service");
        return adminRepository.sitterTotalCount(getUserKey());
    }

    //시터 정보 상세 가져오기
    public HashMap<String, Object> getSitterDetail(Integer userSeq) {
        return adminRepository.getSitterDetail(userSeq);
    }

    //시터 승인
    @Transactional
    public HashMap<String, Object> sitterApproval(Integer userSeq) {
        adminRepository.sitterApproval(userSeq);
        return new HashMap<>();
    }

    //관리자 예약 관리 리스트 가져오기
    public HashMap<String, Object> getReservationManageList(Criteria criteria, String orderBy, String type, String search) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>관리자 예약 관리 리스트 가져오기 Service");
        Pagination pagination = new Pagination();
        pagination.setCriteria(criteria);
        System.out.println(">>>>>>>>>>>>>>>>type"+type);
        System.out.println(">>>>>>>>>>>>>>>>search"+search);
        pagination.setTotalCount(reservationManageTotalCount(type, search));


        if(type.equals("status")) {
            switch (search) {
                case "예약신청": search = "R"; break;
                case "예약거절": search = "RR"; break;
                case "예약수락": search = "RA"; break;
                case "결제완료": search = "PC"; break;
                case "예약실행완료": search = "RC"; break;
                case "예약취소": search = "RCC"; break;
                case "환불요청": search = "RRQ"; break;
                case "환불완료": search = "RCP"; break;
            }
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("pageStart", criteria.getPageStart());
        data.put("perPageNum", criteria.getPerPageNum());
        data.put("orderBy", orderBy);
        data.put("type", type);
        data.put("search", search);

        return new HashMap<>() {{
            put("reservationList", adminRepository.getReservationManageList(data));
            put("page", criteria.getPage());
            put("totalCount", pagination.getTotalCount());
            put("startPage", pagination.getStartPage());
            put("endPage", pagination.getEndPage());
        }};
    }

    //관리자 예약 총 수
    public Integer reservationManageTotalCount(String type, String search) {
        System.out.println(">>>>>>>>관리자 예약 총 수 가져오기 Service");

        HashMap<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("search", search);
//        data.put("pageStart", pageStart);
//        data.put("perPageNum", perPageNum);

        return adminRepository.reservationManageTotalCount(data);
    }

    //관리자 예약 취소
    @Transactional
    public HashMap<String, Object> reservationCancel(Integer reservationSeq) {
        System.out.println(">>>>>>>>관리자 예약 취소 Service");

        petSitService.cancelReservation(reservationSeq);

        return new HashMap<>();
    }

    //관리자 환불 처리
    @Transactional
    public HashMap<String, Object> refundConfirm(Integer reservationSeq) {
        System.out.println(">>>>>>>>관리자 환불 처리 Service");

        //결제 테이블 상태 변경
        adminRepository.refundConfirm(reservationSeq);

        //예약 상태 변경
        petSitRepository.changeReservationStatus("RCP", reservationSeq);

        return new HashMap<>();
    }


}
