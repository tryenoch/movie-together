package com.bitc.full505_team2_project.mapper;

import com.bitc.full505_team2_project.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

//DAO 대신 Mapper 만듬 Mapper 사용 이유:일일이 DAO를 만들지 않고 인터페이스 만을 이용해 좀더 편하게 개발할수있음
//매퍼 인터페이스와 XML 따로 만들기
@Mapper
public interface MemberMapper {
    void memberJoin(MemberDto member);

    int loginCheck(String memberId, String memberPw);

    public MemberDto selectMember(String memberId,String memberPw);

    public int IdCheck(String ID1);

    public void Correction(MemberDto memberDto);

    public String selectLikeList(String memberId);
}
