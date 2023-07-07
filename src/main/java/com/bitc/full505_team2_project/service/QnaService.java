package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.dto.BoardDto;
import com.bitc.full505_team2_project.dto.BoardFileDto;
import com.bitc.full505_team2_project.dto.QnaDto;
import com.bitc.full505_team2_project.dto.QnaFileDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Service
public interface QnaService {
  // qna 게시글 목록
  List<QnaDto> selectQnaList() throws Exception;

  // qna 게시글 상세보기
  QnaDto selectQnaDetail(int qnaPk) throws Exception;

  // qna 게시글 등록
  void insertQna(QnaDto qnaBoard, MultipartHttpServletRequest multipart) throws Exception;

  // qna 게시글 수정
  void updateQna(QnaDto qnaBoard) throws Exception;

  // qna 게시글 삭제
  void deleteQna(int qnaPk) throws Exception;

  // 다운로드 할 파일 정보 가져오기
  QnaFileDto selectQnaFileInfo(int qnaFileId, int qnaPk) throws Exception;
}
