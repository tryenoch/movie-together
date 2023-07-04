package com.bitc.full505_team2_project.dto;

import lombok.Data;

import java.util.List;

@Data
public class QnaDto {
  private String qnaTitle;
  private String qnaWriter;
  private String qnaContent;
  private String qnaDate;
  private int qnaCnt;
  private int qnaCategory;
  private String qnaDeleteYn;

  private List<QnaFileDto> fileList;
}
