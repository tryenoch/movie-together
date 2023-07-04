package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.dto.BoardDto;
import com.bitc.full505_team2_project.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/board")
public class BoardController {

  @Autowired
  private BoardService boardService;

  /* 게시글 리스트 */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
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
}
