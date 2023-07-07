package com.bitc.full505_team2_project.mapper;

import com.bitc.full505_team2_project.dto.MovieDTO;
import com.bitc.full505_team2_project.dto.ReviewCardDto;
import com.bitc.full505_team2_project.dto.ReviewDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

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
    List<ReviewDto> getAllReviewList(ReviewCardDto dto);
    List<ReviewDto> getMyReviewList(ReviewCardDto dto);
    void writeReview(ReviewDto dto);
    void editReview(ReviewDto dto);
    void delReview(ReviewDto dto);
}
