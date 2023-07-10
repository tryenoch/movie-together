
package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.dto.BoardDto;
import com.bitc.full505_team2_project.dto.BoardFileDto;
import com.bitc.full505_team2_project.dto.CommentDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Service
public interface BoardService {
  // 게시글 목록
  List<BoardDto> selectBoardList(int pageNum) throws Exception;

  // 검색 키워드 글 목록
  List<BoardDto> selectSearchList(int pageNum, String searchKey, String searchItem) throws Exception;

  // 게시글 상세보기
  BoardDto selectBoardDetail(int boardPk) throws Exception;

  // 게시판 글 등록
  void insertBoard(BoardDto board, MultipartHttpServletRequest multipart) throws Exception;

  // 게시판 글 수정
  void updateBoard(BoardDto board) throws Exception;

  // 게시판 글 삭제
  void deleteBoard(int boardPk) throws Exception;

  /* 코멘트 관련 메소드 */

  // 코멘트 등록
  void insertComment(CommentDto comment) throws Exception;

  // 코멘트 리스트 불러오기
  List<CommentDto> selectCommentList (@Param("commentNum") int boardPk) throws Exception;

  // 코멘트 삭제
  void deleteComment(@Param("commentNum") int boardPk, @Param("commentPk") int commentPk) throws Exception;

  // 다운로드 할 파일 정보 가져오기
  BoardFileDto selectBoardFileInfo(int boardFileId, int boardPk) throws Exception;
}

