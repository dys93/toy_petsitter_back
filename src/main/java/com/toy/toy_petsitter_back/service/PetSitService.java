package com.toy.toy_petsitter_back.service;

import com.toy.toy_petsitter_back.DTO.Criteria;
import com.toy.toy_petsitter_back.DTO.Pagination;
import com.toy.toy_petsitter_back.repository.PetSitRepository;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

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
    public HashMap<String, Object> getPostList(Criteria criteria) {
        Pagination pagination = new Pagination();
        pagination.setCriteria(criteria); //현재 페이지 //한 페이지당 보여 줄 게시글의 갯수
        pagination.setTotalCount(totalCount()); //총 게시글 수

        System.out.println(">>>>>>>>>>>>>>>pagination1:"+ pagination.getTotalCount());
        System.out.println(">>>>>>>>>>>>>>>pagination2:"+ pagination.getStartPage());
        System.out.println(">>>>>>>>>>>>>>>pagination3:"+ pagination.getEndPage());
        System.out.println(">>>>>>>>>>>>>>>pagination4:"+ pagination.getDisplayPageNum());

        return new HashMap<>() {{
            put("list", petSitRepository.getPostList(criteria));
            put("totalCount", pagination.getTotalCount());
            put("startPage", pagination.getStartPage());
            put("endPage", pagination.getEndPage());
        }};
    }

    //전체 데이터 갯수 가져오기
    public Integer totalCount() {
        return petSitRepository.totalCount();
    }

    //게시글 상세 가져오기
    public HashMap<String, Object> getPostDetail(Integer petsitSeq) {

        System.out.println(">>>>>>>>>>>>>>게시글 상세 Service");

        return petSitRepository.getPostDetail(petsitSeq);

    }


}
