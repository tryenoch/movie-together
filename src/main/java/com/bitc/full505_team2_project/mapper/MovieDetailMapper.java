package com.bitc.full505_team2_project.mapper;

import com.bitc.full505_team2_project.dto.MovieDTO;
import com.bitc.full505_team2_project.dto.ReviewCardDto;
import com.bitc.full505_team2_project.dto.ReviewDto;
import com.bitc.full505_team2_project.dto.TheaterDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MovieDetailMapper {
    void makeData(String movieTitle) throws Exception;
    int getMoviePk(String movieTitle) throws Exception;
    MovieDTO selectMovieInfo(int moviePk) throws Exception;
    List<String> getAreas(String type) throws Exception;
    List<TheaterDto> getTheaters(String type, String area) throws Exception;
    String getLikedList(String id) throws Exception;
    void setLikedList(String id, String likedList) throws Exception;
    void plusLikeCnt(int moviePk);
    void minusLikeCnt(int moviePk);
    int likeCnt(int moviePk);
    void setReviewLikedList(String id, String likedList) throws Exception;
    void plusReviewLikeCnt(int reviewPk);
    void minusReviewLikeCnt(int reviewPk);
    int reviewLikeCnt(int reviewPk);
    List<ReviewDto> getAllReviewList(ReviewCardDto dto);
    List<ReviewDto> getMyReviewList(ReviewCardDto dto);
    void writeReview(ReviewDto dto);
    void editReview(ReviewDto dto);
    void delReview(ReviewDto dto);
    void delTheater(TheaterDto dto);
    List<TheaterDto> getTheater(String type);
    void addTheater(TheaterDto dto);
    String getReviewLike(String id);
}
