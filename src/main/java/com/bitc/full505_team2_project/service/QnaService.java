package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.dto.*;
import org.apache.catalina.startup.Catalina;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public interface QnaService {
  // qna 게시글 목록
  List<QnaDto> selectQnaList(int pageNum) throws Exception;

  // 검색 키워드 글 목록
  List<QnaDto> selectSearchList(int pageNum, String searchKey, String searchItem) throws Exception;

  // qna 카테고리 목록
  List<CategoryDto> categoryList() throws Exception;

  // qna 카테고리별 게시글 목록
  List<QnaDto> selectQnaCategoryList(int qnaCategory) throws Exception;

  // qna 게시글 상세보기
  QnaDto selectQnaDetail(int qnaPk) throws Exception;

  // qna 게시글 등록
  void insertQna(QnaDto qnaBoard, MultipartHttpServletRequest multipart) throws Exception;

  // qna 게시글 수정
  void updateQna(QnaDto qnaBoard) throws Exception;

  // qna 게시글 삭제
  void deleteQna(int qnaPk) throws Exception;


  /* 코멘트 관련 메소드 */

  void insertComment(CommentDto comment) throws Exception;

  // 코멘트 리스트 불러오기
  List<CommentDto> selectCommentList (@Param("commentNum") int qnaPk) throws Exception;

  // 코멘트 삭제
  void deleteComment(@Param("commentNum") int qnaPk, @Param("commentPk") int commentPk) throws Exception;


  // 다운로드 할 파일 정보 가져오기
  BoardFileDto selectQnaFileInfo(int qnaFileId, int qnaPk) throws Exception;
}
