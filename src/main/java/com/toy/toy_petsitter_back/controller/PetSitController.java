package com.toy.toy_petsitter_back.controller;

import com.toy.toy_petsitter_back.DTO.Criteria;
import com.toy.toy_petsitter_back.auth.AuthCheck;
import com.toy.toy_petsitter_back.response.RestResponse;
import com.toy.toy_petsitter_back.service.PetSitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RequestMapping("/api/v1/petSit")
@RestController
public class PetSitController extends BaseController {
    private final PetSitService petSitService;

    public PetSitController(PetSitService petSitService) {
        this.petSitService = petSitService;
    }

    /**
     * 내 작성글 저장
     */
    @AuthCheck(role = AuthCheck.Role.PET_SITTER)
    @RequestMapping(value = "/saveMyPost", method = RequestMethod.POST)
    public ResponseEntity<?> saveMyPost() {
        return new RestResponse().ok().setBody(petSitService.saveMyPost(getParameter("title"), getParameter("contents"), getParameter("petYn"),
                getParameter("pickupYn"), getParameter("largeDogYn"), getParameterOrNull("yardYn"), getParameter("oldDogYn"),
                getParameterOrNull("priceLarge"), getParameterOrNull("priceMedium"), getParameterOrNull("priceSmall"))).responseEntity();
    }

    /**
     * 내 작성글 불러오기
     */
    @AuthCheck(role = AuthCheck.Role.PET_SITTER)
    @RequestMapping(value = "/getMyPost", method = RequestMethod.GET)
    public ResponseEntity<?> getMyPost() {
        return new RestResponse().ok().setBody(petSitService.getMyPost()).responseEntity();
    }

    /**
     * 내 작성글 수정하기
     */
    @AuthCheck(role = AuthCheck.Role.PET_SITTER)
    @RequestMapping(value = "/modifyMyPost", method = RequestMethod.POST)
    public ResponseEntity<?> modifyMyPost() {
        return new RestResponse().ok().setBody(petSitService.modifyMyPost(getParameter("title"), getParameter("contents"), getParameter("petYn"),
                getParameter("pickupYn"), getParameter("largeDogYn"), getParameterOrNull("yardYn"), getParameter("oldDogYn"),
                getParameterOrNull("priceLarge"), getParameterOrNull("priceMedium"), getParameterOrNull("priceSmall"))).responseEntity();
    }

    /**
     * 내 작성글 삭제하기
     */
    @AuthCheck(role = AuthCheck.Role.PET_SITTER)
    @RequestMapping(value = "/deleteMyPost", method = RequestMethod.POST)
    public ResponseEntity<?> deleteMyPost() {
        return new RestResponse().ok().setBody(petSitService.deleteMyPost()).responseEntity();
    }


    /**
     * 게시글 불러오기
     */
    @AuthCheck(role = AuthCheck.Role.NONE)
    @RequestMapping(value = "/getPostList", method = RequestMethod.GET)
    public ResponseEntity<?> getPostList() {

        Criteria criteria = new Criteria();
        criteria.setPerPageNum(10);
        criteria.setPage(Integer.parseInt(getParameter("pageNum"))); //현재 페이지

        System.out.println(">>>>>>>>>>>>>>>petSitService.getPostList(criteria)"+petSitService.getPostList(criteria));


        return new RestResponse().ok().setBody(petSitService.getPostList(criteria)).responseEntity();
    }

    /**
     * 게시글 상세(예약페이지) 불러오기
     */
    @AuthCheck(role = AuthCheck.Role.NONE)
    @RequestMapping(value = "/getPostDetail", method = RequestMethod.GET)
    public ResponseEntity<?> getPostDetail() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>게시글 상세 Controller");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>petsitSeq: "+getParameter("petSitSeq"));
        return new RestResponse().ok().setBody(petSitService.getPostDetail(Integer.parseInt(getParameter("petSitSeq")))).responseEntity();
    }

    /**
     * 리뷰 불러오기
     */
    @AuthCheck(role = AuthCheck.Role.NONE)
    @RequestMapping(value = "/getReview", method = RequestMethod.GET)
    public ResponseEntity<?> getReview() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>리뷰 불러오기 Controller");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>petsitSeq: "+getParameter("petSitSeq"));

        Criteria criteria = new Criteria();
        criteria.setPerPageNum(5);
        criteria.setPage(Integer.parseInt(getParameter("pageNum"))); //현재 페이지

        return new RestResponse().ok().setBody(petSitService.getReview(criteria,Integer.parseInt(getParameter("petSitSeq")))).responseEntity();
    }

    /**
     *  예약 요청
     */
    @AuthCheck(role = AuthCheck.Role.USER)
    @RequestMapping(value = "/requestReservation", method = RequestMethod.POST)
    public ResponseEntity<?> requestReservation() {
        return new RestResponse().ok().setBody(petSitService.requestReservation(getParameter("checkIn"), getParameter("checkOut"),
                Integer.parseInt(getParameter("price")), Integer.parseInt(getParameter("smallCnt")), Integer.parseInt(getParameter("mediumCnt")),
                Integer.parseInt(getParameter("largeCnt")), Integer.parseInt(getParameter("petSitSeq")))).responseEntity();
    }

}
