package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.common.ScriptUtils;
import com.bitc.full505_team2_project.dto.BoardDto;
import com.bitc.full505_team2_project.service.BoardService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/board")
public class BoardController {

  @Autowired
  private BoardService boardService;

  /* 게시글 리스트 */
  @RequestMapping(value = {"/list", "/" }, method = RequestMethod.GET)
  public ModelAndView boardList() throws Exception {
    ModelAndView mv = new ModelAndView("board/boardList");

    List<BoardDto> boardList = boardService.selectBoardList();

    mv.addObject("boardList", boardList);

    return mv;
  }

  /* 게시글 상세 보기 */
  @RequestMapping(value = "{boardPk}", method = RequestMethod.GET)
  public ModelAndView boardDetail(@PathVariable("boardPk") int boardPk) throws Exception{
    ModelAndView mv = new ModelAndView("board/boardDetail");

    BoardDto board = boardService.selectBoardDetail(boardPk);
    mv.addObject("board", board);

    return mv;
  }

  /* 게시글 작성하기 */
  @RequestMapping(value = "/write", method = RequestMethod.GET)
  public String boardWriteView() throws Exception{
    return "board/boardWrite";
  }

  /* 게시글 작성 프로세스 (첨부파일 포함) */
  @RequestMapping(value = "/write", method = RequestMethod.POST)
  public String boardWriteProcess(BoardDto board, MultipartHttpServletRequest multipart) throws Exception {
    boardService.insertBoard(board, multipart);
    return "redirect:/board/list";
  }

  /* 게시물 수정 뷰 */
  @RequestMapping(value = "/update/{boardPk}", method = RequestMethod.GET)
  public ModelAndView boardUpdate(@PathVariable("boardPk") int boardPk) throws Exception {
    ModelAndView mv = new ModelAndView("board/boardUpdate");

    BoardDto board = boardService.selectBoardDetail(boardPk);
    mv.addObject("board", board);

    return mv;
  }

  /* 게시물 수정 기능 */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public Object boardUpdateProcess(BoardDto board) throws Exception{
    int boardPk = board.getBoardPk();
    boardService.updateBoard(board);

    // 수정한 페이지로 redirect
    return "redirect:/board/" + boardPk;
  }

  /* 게시물 삭제 기능 */
  @RequestMapping(value = "/delete/{boardPk}", method = RequestMethod.GET)
  public void boardDeleteProcess(@PathVariable("boardPk") int boardPk, HttpServletResponse response) throws Exception {

    /*int pk = Integer.parseInt(boardPk);*/

    boardService.deleteBoard(boardPk);
    //ScriptUtils.alert(response, "삭제 되었습니다.");

    ScriptUtils.alertAndMovePage(response, "삭제 되었습니다.", "/board/list");

  }

}
