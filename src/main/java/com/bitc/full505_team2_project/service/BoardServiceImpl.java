package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.common.FileUtils;
import com.bitc.full505_team2_project.dto.BoardDto;
import com.bitc.full505_team2_project.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService{

  // BoardMapper 인터페이스 타입의 변수 선언
  @Autowired
  private BoardMapper boardMapper;

  @Autowired
  private FileUtils fileUtils;

  @Override
  public List<BoardDto> selectBoardList() throws Exception {
    return boardMapper.selectBoardList();
  }

  @Override
  public BoardDto selectBoardDetail(int boardPk) throws Exception {
    return null;
  }

  @Override
  public void insertBoard(BoardDto board, MultipartHttpServletRequest multipart) throws Exception {

  }

  @Override
  public void updateBoard(BoardDto board) throws Exception {

  }

  @Override
  public void deleteBoard(int boardPk) throws Exception {

  }

  @Override
  public BoardDto selectBoardFile(int idx, int boardPk) throws Exception {
    return null;
  }
}
