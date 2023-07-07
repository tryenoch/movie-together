package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.dto.MemberDto;
import jakarta.servlet.http.HttpSession;

public interface MemberService {



    void memberJoin (MemberDto member);

    public int loginCheck(String memberId,String memberPw);
    public void logout(HttpSession session);

    public MemberDto selectMember(String memberId,String memberPw);

    public int IdCheck(String Id1);


    public void Correction(MemberDto memberDto);

////    주석처리 해야할수도 있는것
//    boolean loginCheck(MemberDto memberDto);
//
//    public MemberDto findByMemberEmail(String loginEmail) {
//        return memberReposio
//    }
}
