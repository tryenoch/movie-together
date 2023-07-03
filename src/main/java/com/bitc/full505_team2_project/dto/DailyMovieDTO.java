package com.bitc.full505_team2_project.dto;

import lombok.Data;

@Data
public class DailyMovieDTO {
//    private String rank; // 일간 영화 랭킹
//    private String movieNm; // 영화 이름
//
//    private String openDt; // 영화 개봉일
//    private String audiAcc; // 누적 관객수
    private String rnum;
    private String rank;
    private String rankInten;
    private String rankOldAndNew;
    private String movieCd;
    private String movieNm;
    private String openDt;
    private String salesAmt;
    private String salesShare;
    private String salesInten;
    private String salesChange;
    private String salesAcc;
    private String audiCnt;
    private String audiInten;
    private String audiChange;
    private String audiAcc;
    private String scrnCnt;
    private String showCnt;
}
