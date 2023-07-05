package com.bitc.full505_team2_project.mapper;

import com.bitc.full505_team2_project.dto.BoardDto;
import com.bitc.full505_team2_project.dto.BoardFileDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 데이터 베이스의 SQL 문과 연동하기 위한 인터페이스
// 해당 인터페이스의 추상 메소드와 xml 파일의 태그명을 1:1로 연동해주는 어노테이션
@Mapper
public interface BoardMapper {
  // 게시판 글 목록
  List<BoardDto> selectBoardList() throws Exception;

  // 게시판 상세 글 확인
  BoardDto selectBoardDetail(int boardPk) throws Exception;

  // 게시판 글 등록
  void insertBoard(BoardDto boardDto) throws Exception;

  // 게시판 글 수정
  void updateBoard(BoardDto board) throws Exception;

  // 게시글 삭제
  void deleteBoard(@Param("boardPk") int boardPk) throws Exception;

  // 조회수 업데이트
  void updateHitCount(@Param("boardPk") int boardPk) throws Exception;

  // 파일 목록 등록
  void insertBoardFileList(List<BoardFileDto> fileList) throws Exception;

  // 파일 목록 불러오기
  List<BoardFileDto> selectBoardFileList(int boardPk) throws Exception;

  // 각 파일 정보 불러오기
  BoardFileDto selectBoardFileInfo(@Param("boardFileId") int boardFileId, @Param("boardPk") int boardPk) throws Exception;


}
