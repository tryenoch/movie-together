package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.dto.MemberDto;
import com.bitc.full505_team2_project.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public void memberJoin(MemberDto member) {
       memberMapper.memberJoin(member);
    }
}
