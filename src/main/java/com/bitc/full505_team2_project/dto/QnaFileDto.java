package com.bitc.full505_team2_project.dto;

import lombok.Data;

@Data
public class QnaFileDto {
  private int qnaFileId;
  private int qnaPk;
  private String qnaOfileName;
  private String qnaSfileName;
  private long qnaFileSize;
  private String qnaCreatedDate;
}
