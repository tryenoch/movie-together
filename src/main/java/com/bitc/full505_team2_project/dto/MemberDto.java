package com.bitc.full505_team2_project.dto;

import lombok.Data;

@Data
public class MemberDto {
  private String memberId;
  private String memberPw;
  private String memberName;
  private String memberRegidate;
  private String memberEmail;
  private String memberBirth;
  private String memberLikeList;
  private int memberGrade;
}
