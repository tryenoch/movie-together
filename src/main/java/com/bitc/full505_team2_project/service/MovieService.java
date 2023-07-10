package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.dto.DailyMovieDTO;
import com.bitc.full505_team2_project.dto.MovieDetailDTO;

import java.util.List;
//(영화 검색, 금일 박스오피스 순위 를 보여주기 위한 서비스),사용하진 않음 예비용
public interface MovieService {
    List<DailyMovieDTO> getDailyMovieList(String url) throws Exception;
    List<MovieDetailDTO> getMovieResarchList(String url) throws Exception;
}
