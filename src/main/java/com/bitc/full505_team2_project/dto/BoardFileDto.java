package com.bitc.full505_team2_project.dto;

import lombok.Data;

@Data
public class BoardFileDto {
  private int boardFileId;
  private int boardPk;
  private String boardOfileName;
  private String boardSfileName;
  private long boardFileSize;
  private String boardCreatedDate;
  private int boardSelect;
}
