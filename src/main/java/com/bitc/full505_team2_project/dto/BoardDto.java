package com.bitc.full505_team2_project.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDto {
  private int boardPk;
  private String boardTitle;
  private String boardWriter;
  private String boardContent;
  private String boardDate;
  private int boardVisitCnt;
  private String boardDeleteYn;

  private List<BoardFileDto> fileList;
}
