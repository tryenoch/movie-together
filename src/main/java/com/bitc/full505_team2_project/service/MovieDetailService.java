package com.bitc.full505_team2_project.service;


import com.bitc.full505_team2_project.dto.MovieDTO;
import com.bitc.full505_team2_project.dto.MovieTimeTableDto;
import com.bitc.full505_team2_project.dto.ReviewDto;
import com.bitc.full505_team2_project.dto.TheaterDto;

import java.util.List;
import java.util.Map;

public interface MovieDetailService {


    void makeData(String movieTitle) throws Exception;
    int getMoviePk(String movieTitle) throws Exception;
    MovieDTO selectMovieInfo(int moviePk) throws Exception;
    List<MovieTimeTableDto> getCgvSchedule(String title, String date,String code) throws Exception;
    List<MovieTimeTableDto> getMegaBoxSchedule(String title, String date,String code) throws Exception;
    List<MovieTimeTableDto> getLotteCinemaSchedule(String title, String date,String code) throws Exception;
    List<String> getAreas(String type) throws Exception;
    List<TheaterDto> getTheaters(String type, String area) throws Exception;
    String getDaumId(String title) throws Exception;
    MovieDTO addDaumInfo(MovieDTO dto, String daumId) throws Exception;
    String getLikedList(String id) throws Exception;
    int setLikedList(String id, String likedList, String pk, boolean type) throws Exception;
    int setReviewLikedList(String id, String likedList, String reviewPk, boolean type) throws Exception;
    List<ReviewDto> getReviewList(int moviePk,int page, int num, String id, String all) throws Exception;
    void writeReview(ReviewDto dto) throws Exception;
    void editReview(ReviewDto dto) throws Exception;
    void delReview(ReviewDto dto) throws Exception;
    void updateCgvTheater() throws Exception;
    void updateMegaBoxTheater() throws Exception;
    void updateLotteCinemaTheater() throws Exception;
    String getReviewLike(String id) throws Exception;
}
