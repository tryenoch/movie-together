package com.bitc.full505_team2_project.mapper;

import com.bitc.full505_team2_project.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

// 데이터 베이스의 SQL 문과 연동하기 위한 인터페이스
// 해당 인터페이스의 추상 메소드와 xml 파일의 태그명을 1:1로 연동해주는 어노테이션
@Mapper
public interface QnaMapper {
  // 게시판 글 목록
  List<QnaDto> selectQnaList() throws Exception;

  // 카테고리 길이
  List<CategoryDto> categoryList() throws Exception;

  // 카테고리별 글 목록
  List<QnaDto> selectQnaCategoryList(int qnaCategory) throws Exception;

  // 게시판 상세 글 확인
  QnaDto selectQnaDetail(int qnaPk) throws Exception;

  // 게시판 글 등록
  void insertQna(QnaDto qnaDto) throws Exception;

  // 게시판 글 수정
  void updateQna(QnaDto qnaBoard) throws Exception;

  // 게시글 삭제
  void deleteQna(@Param("qnaPk") int qnaPk) throws Exception;

  // 조회수 업데이트
  void updateHitCount(@Param("qnaPk") int qnaPk) throws Exception;

  /* 코멘트 관련 메소드 */

  // 코멘트 등록
  void insertComment(CommentDto comment) throws Exception;

  // 코멘트 리스트 불러오기
  List<CommentDto> selectCommentList (@Param("commentNum") int qnaPk) throws Exception;

  // 코멘트 삭제
  void deleteComment(@Param("commentNum") int qnaPk, @Param("commentPk") int commentPk) throws Exception;


  /* 파일 관련 메소드 */
  // 파일 목록 등록
  void insertQnaFileList(List<BoardFileDto> fileList) throws Exception;

  // 파일 목록 불러오기
  List<BoardFileDto> selectQnaFileList(int qnaPk) throws Exception;

  // 각 파일 정보 불러오기
  BoardFileDto selectQnaFileInfo(@Param("boardFileId") int qnaFileId, @Param("boardPk") int qnaPk) throws Exception;


}
