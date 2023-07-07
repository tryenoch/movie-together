package com.bitc.full505_team2_project.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class MovieDTO {
    private MovieResultDTO movieResultDTO;

    // DB에 있는 movie table에 있는 컬럼
    private int moviePk;
    private String movieTitle;
    private int movieLikeCnt;

    // 다음검색결과로 가져오는 데이터
    private ArrayList<String> genres;
    private ArrayList<String> nations;
    private String gradeAge;
    private String runningTime;
    private String audience;
    private String boxOfficeRank;
    private ArrayList<String> casts;
    private String releaseDate;
}