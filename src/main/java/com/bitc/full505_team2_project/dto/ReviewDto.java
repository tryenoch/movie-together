package com.bitc.full505_team2_project.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReviewDto {
  private int reviewPk;
  private String reviewTitle;
  private String reviewWriter;
  private String reviewContent;
  private String reviewDate;
  private int reviewLikeCnt;
  private String reviewMoviePk;
  private int reviewEdit;
  private String reviewLikedId;
  private int reviewStar;


//  private List<BoardFileDto> fileList;
}
