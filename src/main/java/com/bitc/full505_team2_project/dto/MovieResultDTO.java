package com.bitc.full505_team2_project.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieResultDTO {
    private String boxOfficeResult;
    private String boxofficeType;
    private String showRange;
    private List<DailyMovieDTO> dailyBoxOfficeList = null;
}
