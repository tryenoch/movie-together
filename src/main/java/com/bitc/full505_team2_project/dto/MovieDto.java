package com.bitc.full505_team2_project.dto;

import lombok.Data;

@Data
public class MovieDTO {
    private MovieResultDTO movieResultDTO;

    // DB에 있는 movie table에 있는 컬럼 추가했는데 여기다가 하는거 아니면 dto클래스 새로추가할게요~ -pjh
    private int moviePk;
    private String movieTitle;
    private int movieLikeCnt;
}
