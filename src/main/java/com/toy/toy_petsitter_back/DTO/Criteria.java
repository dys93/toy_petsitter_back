package com.toy.toy_petsitter_back.DTO;

public class Criteria {

    //현재 페이지 번호
    private int page;

    //한 페이지 당 보여줄 게시글의 갯수
    private int perPageNum;

    //특정 페이지의 게시글 시작 번호(행 번호)
    public int getPageStart(){
        //현재 페이지의 게시글 시작 번호 = (현재 페이지 번호 -1) * 페이지 당 보여줄 게시글 갯수
        return (this.page -1) * perPageNum;
    }

    public Criteria() {
        this.page = 1;
        this.perPageNum = 10;
    }

    //현재 페이지 가져오기?
    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        if(page <= 0) {
            this.page =1;
        } else {
            this.page = page;
        }
    }

    public int getPerPageNum() {
        return this.perPageNum;
    }

    public void setPerPageNum(int pageCount) {
        System.out.println(">>>>>>>>>setPerPageNum: pageCount"+pageCount);
        int count = this.perPageNum;

        if(pageCount != count) {
            System.out.println(">>>>>>>>>count"+count);
            this.perPageNum = pageCount;
//            this.perPageNum = count;
        } else {
            System.out.println(">>>>>>>>>pageCount"+pageCount);
//            this.perPageNum = pageCount;
            this.perPageNum = count;
        }
    }
}
