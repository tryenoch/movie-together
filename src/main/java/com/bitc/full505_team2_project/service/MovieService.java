package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.dto.DailyMovieDTO;

import java.util.List;

public interface MovieService {
    List<DailyMovieDTO> getDailyMovieList(String url) throws Exception;
}
