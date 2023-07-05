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



//
////  로그인에 쓰일 getter setter 아이디
//  public String getMemberid() {
//    return memberId;
//  }
//  public void setMemberid(String memberId) {
//    this.memberId = memberId;
//  }
//
//
//
//  //  로그인에 쓰일 getter setter 비밀번호
//  public String getMemberpw() {
//    return memberPw;
//  }
//  public void setMemberpw(String memberPw) {
//    this.memberPw = memberPw;
//  }
//
//  @Override
//  public String toString() {
//    return "MemberDto[memberId=" + memberId + ", memberPw = " + memberPw + "]";
//  }
}
