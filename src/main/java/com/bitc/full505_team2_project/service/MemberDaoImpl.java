package com.bitc.full505_team2_project.service;


import com.bitc.full505_team2_project.dto.MemberDao;
import com.bitc.full505_team2_project.dto.MemberDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository  // 현재 클래스를 dao bean으로 등록
public class MemberDaoImpl implements MemberDao {

    @Inject  // SqlSession 의존관계 투입
    SqlSession sqlSession;

    @Override
    public String loginCheck(MemberDto dto) {
        return sqlSession.selectOne("member.login_check",dto);
    }
}
