package com.toy.toy_petsitter_back.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {

    private Criteria criteria;
    private int totalCount; //총 게시글 수
    private int startPage; //화면에 보여질 첫번째 페이지 번호, 시작 페이지 번호
    private int endPage; //화면에 보여질 마지막 페이지 번호, 끝 페이지 번호
    private boolean prev; //이전 버튼 생성 여부
    private boolean next; //다음 버튼 생성 여부
    private int displayPageNum = 5; //화면 하단에 보여지는 페이지 버튼의 수 // 이전 < [1] [2] [3] [4] [5] > 다음

    public Criteria getCriteria() {
        return criteria;
    }

//    public void setCriteria(Criteria criteria) {
//        this.criteria = criteria;
//    }
//
//    public  int getTotalCount() {
//        return this.totalCount;
//    }

    //데이터의 총 갯수를 구하면 calcData() 메서드를 호출하여 페이징 계산을 한다
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        calcData();
    }

    //계산된 데이터
    private void calcData() {
        //끝 페이지 번호 = (현재 페이지 번호 / 화면에 보여질 페이지 번호의 갯수) * 화면에 보여질 페이지 번호의 갯수
        endPage = (int)(Math.ceil(criteria.getPage() / (double)displayPageNum) * displayPageNum);

        //시작 페이지 번호 = (끝 페이지 번호 - 화면에 보여질 페이지 번호의 갯수) + 1
        startPage = (endPage - displayPageNum) + 1;
        if(startPage <= 0) startPage = 1;

        //마지막 페이지 번호 = 총 게시글 수 / 한 페이지당 보여줄 게시글의 갯수
        int tempEndPage = (int)(Math.ceil(totalCount / (double)criteria.getPerPageNum()));
        if(endPage > tempEndPage) endPage = tempEndPage;

        prev = startPage == 1? false:true; //prev = startPage != 1;
        next = endPage * criteria.getPerPageNum() < totalCount ? true: false; //next = endPage * criteria.getPerPageNum() < totalCount;
    }

//    public int getStartPage() {
//        return this.startPage;
//    }
//    public void setStartPage(int startPage) {
//        this.startPage = startPage;
//    }

//    public int getEndPage() {
//        return this.endPage;
//    }
//    public void setEndPage(int endPage) {
//        this.endPage = endPage;
//    }

    public boolean isPrev() {
        return this.prev;
    }
//    public void setPrev(boolean prev) {
//        this.prev = prev;
//    }
    public boolean isNext() {
        return this.next;
    }
//    public void setNext(boolean next) {
//        this.next = next;
//    }
//    public int getDisplayPageNum() {
//        return this.displayPageNum;
//    }
//    public void setDisplayPageNum(int displayPageNum) {
//        this.displayPageNum = displayPageNum;
//    }

}
