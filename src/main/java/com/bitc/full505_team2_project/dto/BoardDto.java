package com.bitc.full505_team2_project.dto;

import lombok.Data;

@Data
public class BoardDto {
  private int boardPk;
  private String boardTitle;
  private String boardWriter;
  private String boardContent;
  private String boardDate;
  private int boardVisitCnt;
  private String boardOfile;
  private String boardSfile;
  private long boardFileSize;
  private String boardDeleteYn;
}
