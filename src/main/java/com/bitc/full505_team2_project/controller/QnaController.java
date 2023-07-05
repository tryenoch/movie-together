package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.dto.BoardDto;
import com.bitc.full505_team2_project.dto.QnaDto;
import com.bitc.full505_team2_project.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/qna")
public class QnaController {
  @Autowired
  private QnaService qnaService;

  /* qna 게시글 리스트 */
  @RequestMapping(value = {"/list", "/" }, method = RequestMethod.GET)
  public ModelAndView boardList() throws Exception {
    ModelAndView mv = new ModelAndView("qna/qnaList");

    List<QnaDto> qnaList = qnaService.selectQnaList();

    mv.addObject("qnaList", qnaList);

    return mv;
  }

}
