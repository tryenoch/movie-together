package com.bitc.full505_team2_project.service;


import com.bitc.full505_team2_project.dto.MovieDto;

public interface MovieDetailService {
    void makeData(String movieTitle) throws Exception;
    int getMoviePk(String movieTitle) throws Exception;
    MovieDto selectMovieInfo(int moviePk) throws Exception;
}
