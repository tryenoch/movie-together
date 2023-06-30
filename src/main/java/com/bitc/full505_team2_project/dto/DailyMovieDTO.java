package com.bitc.full505_team2_project.dto;

import lombok.Data;

@Data
public class DailyMovieDTO {
    private String rank; // 일간 영화 랭킹
    private String movieNm; // 영화 이름

    private String openDt; // 영화 개봉일
    private String audiAcc; // 누적 관객수
}
