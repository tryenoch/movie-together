package com.bitc.full505_team2_project.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QnaDto {
  private int qnaPk;
  private String qnaTitle;
  private String qnaWriter;
  private String qnaContent;
  private String qnaDate;
  private int qnaCnt;
  private int qnaCategory;
  private String qnaCategoryName;
  private String qnaDeleteYn;

  private List<BoardFileDto> fileList;

  /* 카테고리 리스트 */
  // private ArrayList<Integer> categoryPk;
}
