package com.bitc.full505_team2_project.mapper;

import com.bitc.full505_team2_project.dto.MovieDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieDetailMapper {
    void makeData(String movieTitle) throws Exception;
    int getMoviePk(String movieTitle) throws Exception;
    MovieDto selectMovieInfo(int moviePk) throws Exception;
}
