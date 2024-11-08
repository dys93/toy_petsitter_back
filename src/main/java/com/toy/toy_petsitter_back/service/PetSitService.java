package com.toy.toy_petsitter_back.service;

import com.toy.toy_petsitter_back.DTO.Criteria;
import com.toy.toy_petsitter_back.DTO.Pagination;
import com.toy.toy_petsitter_back.repository.PetSitRepository;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Primary
@Service
public class PetSitService extends BaseService {

    PetSitService(PetSitRepository petSitRepository) {
        this.petSitRepository = petSitRepository;
    }

    private final PetSitRepository petSitRepository ;

    //내 작성글 저장
    @Transactional
    @SneakyThrows
    public HashMap<String, Object> saveMyPost(String title, String content, String petYn, String pickupYn, String largeDogYn, String yardYn,
                                          String oldDogYn, String priceLarge, String priceMedium, String priceSmall) {

        Integer userKey = getUserKey();

        //Service로 넘길 data
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("content", content);
        data.put("petYn", petYn);
        data.put("pickupYn", pickupYn);
        data.put("largeDogYn", largeDogYn);
        data.put("yardYn", yardYn);
        data.put("oldDogYn", oldDogYn);
        data.put("priceLarge", priceLarge);
        data.put("priceMedium", priceMedium);
        data.put("priceSmall", priceSmall);
        data.put("userKey", userKey);

        //게시글 저장
        petSitRepository.insertMyPost(data);

        //게시글 작성 상태 저장
        petSitRepository.updatePostStatus("Y", userKey);

        return new HashMap<>();
    }

    //내 작성글 불러오기
    public HashMap<String, Object> getMyPost() {

        System.out.println(">>>>>>>>>>getMyPost() Service");

        System.out.println(">>>>>>>>>>getUserKey()"+getUserKey());

        return petSitRepository.getMyPost(getUserKey());
    }

    //내 작성글 수정하기
    @Transactional
    @SneakyThrows
    public HashMap<String, Object> modifyMyPost(String title, String content, String petYn, String pickupYn, String largeDogYn, String yardYn,
                                              String oldDogYn, String priceLarge, String priceMedium, String priceSmall) {

        System.out.println(">>>>>>>>>>modifyMyPost() Service");

        //Service로 넘길 data
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("content", content);
        data.put("petYn", petYn);
        data.put("pickupYn", pickupYn);
        data.put("largeDogYn", largeDogYn);
        data.put("yardYn", yardYn);
        data.put("oldDogYn", oldDogYn);
        data.put("priceLarge", priceLarge);
        data.put("priceMedium", priceMedium);
        data.put("priceSmall", priceSmall);
        data.put("userKey", getUserKey());

        //작성글 수정
        petSitRepository.updateMyPost(data);

        return new HashMap<>();
    }

    //내 작성글 삭제하기
    public HashMap<String, Object> deleteMyPost() {
        System.out.println(">>>>>>>>작성글 삭제 Service");
        petSitRepository.deleteMyPost(getUserKey());
        return new HashMap<>();
    }

    //작성글 리스트 가져오기
    public HashMap<String, Object> getPostList(Criteria criteria, String orderBy, String petYn, String pickupYn,
                                               String largeDogYn, String yardYn, String oldDogYn, String search) {
        Pagination pagination = new Pagination();
        pagination.setCriteria(criteria); //현재 페이지 //한 페이지당 보여 줄 게시글의 갯수
        pagination.setTotalCount(totalCount(petYn, pickupYn, largeDogYn, yardYn, oldDogYn, search)); //총 게시글 수

        HashMap<String, Object> data = new HashMap<>();
        data.put("pageStart", criteria.getPageStart());
        data.put("perPageNum", criteria.getPerPageNum());
        data.put("orderBy", orderBy);
        if(petYn != "") data.put("petYn", "Y");
        if(pickupYn != "") data.put("pickupYn", "Y");
        if(largeDogYn != "") data.put("largeDogYn", "Y");
        if(yardYn != "") data.put("yardYn", "Y");
        if(oldDogYn != "") data.put("oldDogYn", "Y");
        if(search != "") data.put("search", search);

        return new HashMap<>() {{
            put("list", petSitRepository.getPostList(data));
            put("page", criteria.getPage());
            put("totalCount", pagination.getTotalCount());
            put("startPage", pagination.getStartPage());
            put("endPage", pagination.getEndPage());
        }};
    }

    //게시글 전체 데이터 갯수 가져오기
    public Integer totalCount(String petYn, String pickupYn,
                              String largeDogYn, String yardYn, String oldDogYn, String search) {

        HashMap<String, Object> data = new HashMap<>();
        if(petYn != "") data.put("petYn", "Y");
        if(pickupYn != "") data.put("pickupYn", "Y");
        if(largeDogYn != "") data.put("largeDogYn", "Y");
        if(yardYn != "") data.put("yardYn", "Y");
        if(oldDogYn != "") data.put("oldDogYn", "Y");
        if(search != "") data.put("search", search);

        return petSitRepository.totalCount(data);
    }

    //게시글 상세 가져오기
    public HashMap<String, Object> getPostDetail(Integer petsitSeq) {

        System.out.println(">>>>>>>>>>>>>>게시글 상세 Service");

        return petSitRepository.getPostDetail(petsitSeq);

    }

    //리뷰 리스트 가져오기
    public HashMap<String, Object> getReview(Criteria criteria, Integer petsitSeq) {

        System.out.println(">>>>>>>>>>getReview() Service"+criteria);

        Pagination pagination = new Pagination();
        pagination.setCriteria(criteria); //현재 페이지 //한 페이지당 보여 줄 게시글의 갯수
        pagination.setTotalCount(reviewTotalCount(petsitSeq)); //총 게시글 수

        HashMap<String, Object> data = new HashMap<>();
        data.put("pageStart", criteria.getPageStart());
        data.put("perPageNum", criteria.getPerPageNum());
        data.put("petsitSeq", petsitSeq);

        return new HashMap<>() {{
            put("reviewList", petSitRepository.getReview(data));
            put("page", criteria.getPage());
            put("totalCount", pagination.getTotalCount());
            put("startPage", pagination.getStartPage());
            put("endPage", pagination.getEndPage());
        }};
    }

    //리뷰 전체 데이터 갯수 가져오기
    public Integer reviewTotalCount(Integer petsitSeq) {
        return petSitRepository.reviewTotalCount(petsitSeq);
    }

    //예약 요청 저장하기
    public HashMap<String, Object> requestReservation(String checkIn, String checkOut, Integer price, Integer smallCnt,
                                                      Integer mediumCnt, Integer largeCnt, Integer petSitSeq) {

        //글작성한 sitter_seq 가져오기
        Integer sitterSeq = petSitRepository.selectSitterSeq(petSitSeq);

        HashMap<String, Object> data = new HashMap<>();
        data.put("checkIn",checkIn);
        data.put("checkOut",checkOut);
        data.put("price",price);
        data.put("smallCnt",smallCnt);
        data.put("mediumCnt",mediumCnt);
        data.put("largeCnt",largeCnt);
        data.put("petSitSeq",petSitSeq);
        data.put("userSeq", getUserKey());
        data.put("sitterSeq", sitterSeq);

        //예약 요청 저장
        petSitRepository.insertReservation(data);

        return new HashMap<>();

    }

    //예약 내역 리스트 가져오기
    public HashMap<String, Object> getReservationList(Criteria criteria) {
        Pagination pagination = new Pagination();
        pagination.setCriteria(criteria); //현재 페이지 //한 페이지당 보여 줄 게시글의 갯수
        pagination.setTotalCount(reservationTotalCount(getUserKey())); //총 예약 수

        HashMap<String, Object> data = new HashMap<>();
        data.put("pageStart", criteria.getPageStart());
        data.put("perPageNum", criteria.getPerPageNum());
        data.put("userSeq", getUserKey());

        return new HashMap<>() {{
            put("reservationList", petSitRepository.getReservationList(data));
            put("page", criteria.getPage());
            put("totalCount", pagination.getTotalCount());
            put("startPage", pagination.getStartPage());
            put("endPage", pagination.getEndPage());
        }};
    }

    //예약 내역 리스트 전체 갯수 가져오기
    public Integer reservationTotalCount(Integer userSeq) {
        return petSitRepository.totalReservationCount(userSeq);
    }

    //결제하기
    public HashMap<String, Object> payment(Integer reservationSeq, String paymentType) {
        System.out.println(">>>>>>>>결제하기 Service");
        // reservation_seq로 관련 데이터 가져오기 : 유저 email, 금액
        HashMap<String, Object> resultReservation = petSitRepository.getReservationInfo(reservationSeq, getUserKey());
        System.out.println(">>>>>>>>>>>>>>>resultReservation"+resultReservation);
        resultReservation.put("paymentType",paymentType);

        petSitRepository.insertPayment(resultReservation);
        petSitRepository.changeReservationStatus("PC" ,reservationSeq);
        return new HashMap<>();
    }

    //취소하기
    public HashMap<String, Object> cancelReservation(Integer reservationSeq) {
        System.out.println(">>>>>>>>취소하기 Service");
        // reservation_seq로 예약 데이터
        HashMap<String, Object> resultReservation = petSitRepository.getReservationInfoOnly(reservationSeq);
        System.out.println(">>>>>>>>>>>>>>>resultReservation"+resultReservation);

        if(resultReservation.get("reservation_status").equals("PC")) {
            System.out.println(">>>>>>>>>>>>>>>환불 필요");
            //환불 필요: 결제 상태 취소Y, 환불 N. (환불 완료 후 환불 Y로 변경)
            petSitRepository.refund(reservationSeq);
            //환불 신청으로 변경
            petSitRepository.changeReservationStatus("RRQ", reservationSeq);
        } else {
            System.out.println(">>>>>>>>>>>>>>>환불 불필요");
            //취소로 상태 변경
            petSitRepository.changeReservationStatus("RCC", reservationSeq);
        }
        return new HashMap<>();
    }

    //리뷰 작성하기
    public HashMap<String, Object> review(Integer reservationSeq, String reviewContent) {
        System.out.println(">>>>>>>>리뷰 작성하기 Service");

        // reservation_seq로 관련 데이터 가져오기
        HashMap<String, Object> resultReservation = petSitRepository.getReservationInfo(reservationSeq, getUserKey());
        resultReservation.put("reviewContent", reviewContent);

        System.out.println(">>>>>>>>>>>>>>>resultReservation"+resultReservation);

        //리뷰 작성
        petSitRepository.review(resultReservation);
        //예약 상태 변경
        petSitRepository.changeReservationStatus("RVC", reservationSeq);

        return new HashMap<>();
    }

    //펫시터 예약 내역 리스트 가져오기
    public HashMap<String, Object> getReservationManageList(Criteria criteria) {
        System.out.println(">>>>>>>>펫시터 예약 내역 리스트 가져오기 Service");
        Pagination pagination = new Pagination();
        pagination.setCriteria(criteria); //현재 페이지 //한 페이지당 보여 줄 게시글의 갯수
        pagination.setTotalCount(reservationManageTotalCount(getUserKey())); //총 예약 수

        HashMap<String, Object> data = new HashMap<>();
        data.put("pageStart", criteria.getPageStart());
        data.put("perPageNum", criteria.getPerPageNum());
        data.put("sitterSeq", getUserKey());

        return new HashMap<>() {{
            put("reservationList", petSitRepository.getReservationManageList(data));
            put("page", criteria.getPage());
            put("totalCount", pagination.getTotalCount());
            put("startPage", pagination.getStartPage());
            put("endPage", pagination.getEndPage());
        }};
    }

    //시터 예약 내역 리스트 전체 갯수 가져오기
    public Integer reservationManageTotalCount(Integer userSeq) {
        System.out.println(">>>>>>>>시터 예약 내역 리스트 전체 갯수 가져오기 Service");
        return petSitRepository.totalReservationManageCount(userSeq);
    }

    //수락 / 거절 선택
    //취소하기
    public HashMap<String, Object> confirmReservation(Integer reservationSeq, String confirm) {
        System.out.println(">>>>>>>>수락 / 거절 선택 Service");


        if(confirm.equals("Y")) {
            //요청 수락 상태로 변경
            petSitRepository.changeReservationStatus("RA", reservationSeq);
        } else {
            //요청 거절 상태로 변경
            petSitRepository.changeReservationStatus("RR", reservationSeq);
        }

        return new HashMap<>();
    }
}
