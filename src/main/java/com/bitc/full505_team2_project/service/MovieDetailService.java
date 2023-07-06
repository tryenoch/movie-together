package com.bitc.full505_team2_project.service;


import com.bitc.full505_team2_project.dto.MovieDTO;
import com.bitc.full505_team2_project.dto.MovieTimeTableDto;

import java.util.List;

public interface MovieDetailService {


    void makeData(String movieTitle) throws Exception;
    int getMoviePk(String movieTitle) throws Exception;
    MovieDTO selectMovieInfo(int moviePk) throws Exception;
    List<MovieTimeTableDto> getCgvSchedule(String title, String date) throws Exception;
    List<MovieTimeTableDto> getMegaBoxSchedule(String title, String date) throws Exception;
    List<MovieTimeTableDto> getLotteCinemaSchedule(String title, String date) throws Exception;
    String getDaumId(String title) throws Exception;
    MovieDTO addDaumInfo(MovieDTO dto, String daumId) throws Exception;
}
