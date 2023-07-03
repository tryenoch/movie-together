package com.bitc.full505_team2_project.mapper;

import com.bitc.full505_team2_project.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    void memberJoin(MemberDto member);
}
