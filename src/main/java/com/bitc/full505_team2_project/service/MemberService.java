package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.dto.MemberDto;
import jakarta.servlet.http.HttpSession;

public interface MemberService {

//*** 서비스를 호출 하면 어떻게 되는지***

//    Service는 두가지 구현해야함

//    1.service interface 안에 제작
//    2.service interface를 구현한 class 만들기 @Autowired,@Override (구현체 = 구현한 클래스)


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
    String selectLikeList(String userId);
}
