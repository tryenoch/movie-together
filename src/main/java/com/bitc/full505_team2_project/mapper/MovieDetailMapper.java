package com.bitc.full505_team2_project.mapper;

import com.bitc.full505_team2_project.dto.MovieDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieDetailMapper {
    void makeData(String movieTitle) throws Exception;
    int getMoviePk(String movieTitle) throws Exception;
    MovieDTO selectMovieInfo(int moviePk) throws Exception;
    String getLikedList(String id) throws Exception;
    void setLikedList(String id, String likedList) throws Exception;
    void plusLikeCnt(int moviePk);
    void minusLikeCnt(int moviePk);
    int likeCnt(int moviePk);
}
