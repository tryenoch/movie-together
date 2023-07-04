package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.common.FileUtils;
import com.bitc.full505_team2_project.dto.BoardDto;
import com.bitc.full505_team2_project.dto.BoardFileDto;
import com.bitc.full505_team2_project.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService{

  // BoardMapper 인터페이스 타입의 변수 선언
  @Autowired
  private BoardMapper boardMapper;
  @Autowired
  private FileUtils fileUtils;

  // 게시글 리스트 불러오기
  @Override
  public List<BoardDto> selectBoardList() throws Exception {
    return boardMapper.selectBoardList();
  }

  // 게시글 상세 보기
  // 첨부파일 목록도 함께 불러오기
  @Override
  public BoardDto selectBoardDetail(int boardPk) throws Exception {

    /*
    * 1. 컨트롤러에서 전달된 게시물 번호 가져오기
    * 2. mapper를 사용하여 DB에서 지정한 게시물의 조회수 업데이트
    * 3. mapper를 사용하여 DB에서 지정한 게시물 정보 가져오기
    * 4. mapper를 사용하여 DB에서 지정한 게시물의 첨부파일 목록 불러오기
    * 5. 가져온 파일 목록을 BoardDto 타입에 저장
    * 6. 가져온 게시물 정보를 컨트롤러로 리턴 */

    boardMapper.updateHitCount(boardPk);

    // 조회된 정보를 생성한 dto에 저장
    BoardDto board = boardMapper.selectBoardDetail(boardPk);

    // 첨부파일 목록 가져오기
    List<BoardFileDto> boardFileList = boardMapper.selectBoardFileList(boardPk);

    // BoardDto 객체에 가져온 첨부파일 리스트를 추가
    board.setFileList(boardFileList);

    return board;
  }

  @Override
  public void insertBoard(BoardDto board, MultipartHttpServletRequest uploadFiles) throws Exception {
    /* 1. 컨트롤러에서 전달된 데이터 가져오기
    * 2. mapper를 사용해서 db에 등록(게시글, 파일 정보 분리) */

    boardMapper.insertBoard(board);

    List<BoardFileDto> fileList = fileUtils.parseFileInfo(board.getBoardPk(), uploadFiles);

    // CollectionUtils : 스프링 프레임워크에서 제공하는 클래스
    if(CollectionUtils.isEmpty(fileList) == false){
      boardMapper.insertBoardFileList(fileList);
    }
  }

  @Override
  public void updateBoard(BoardDto board) throws Exception {

  }

  @Override
  public void deleteBoard(int boardPk) throws Exception {

  }

  @Override
  public BoardFileDto selectBoardFileInfo(int boardFileId, int boardPk) throws Exception {
    return null;
  }
}
