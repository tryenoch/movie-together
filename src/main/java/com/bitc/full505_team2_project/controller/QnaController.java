package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.common.ScriptUtils;
import com.bitc.full505_team2_project.dto.BoardDto;
import com.bitc.full505_team2_project.dto.BoardFileDto;
import com.bitc.full505_team2_project.dto.CategoryDto;
import com.bitc.full505_team2_project.dto.QnaDto;
import com.bitc.full505_team2_project.service.QnaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/qna")
public class QnaController {
  @Autowired
  private QnaService qnaService;

  /* qna 게시글 리스트 */
  @RequestMapping(value = {"/list", "/" }, method = RequestMethod.GET)
  public ModelAndView qnaList(HttpServletRequest req) throws Exception {
    ModelAndView mv = new ModelAndView("qna/qnaList");

    List<QnaDto> qnaList = qnaService.selectQnaList();
    List<CategoryDto> cateList = qnaService.categoryList();

    /* null 값을 반환할 경우 디폴트 값 지정 (안하면 nullPointException 발생) */
    String userName = (req.getSession().getAttribute("userName") == null) ? "" : (String) req.getSession().getAttribute("userName");
    int userGrade = (req.getSession().getAttribute("userGradle") == null) ? 0 :(Integer) req.getSession().getAttribute("userGradle");

    mv.addObject("qnaList", qnaList);
    mv.addObject("cateList", cateList);
    mv.addObject("userName", userName);
    mv.addObject("userGrade", userGrade);

    return mv;
  }

  /* qna 카테고리별 리스트 */
  @ResponseBody
  @RequestMapping(value = "/list/category", method = RequestMethod.GET)
  public List<QnaDto> selectQnaCategoryList(@RequestParam("categoryPk") int categoryPk) throws Exception {
    List<QnaDto> qnaList = qnaService.selectQnaCategoryList(categoryPk);

    return qnaList;
  }

  /* qna 게시글 상세보기 */
  @RequestMapping(value = "{qnaPk}", method = RequestMethod.GET)
  public ModelAndView qnaDetail(@PathVariable("qnaPk") int qnaPk) throws Exception {
    ModelAndView mv = new ModelAndView("qna/qnaDetail");

    QnaDto qnaBoard = qnaService.selectQnaDetail(qnaPk);
    mv.addObject("qnaBoard", qnaBoard);

    return mv;
  }

  /* qna 게시글 쓰기 뷰 */
  @RequestMapping(value = "/write", method = RequestMethod.GET)
  public String qnaWriteView() throws Exception {
    return "qna/qnaWrite";
  }

  /* qna 게시글 쓰기 프로세스 */
  @RequestMapping(value = "/write", method = RequestMethod.POST)
  public String qnaWriteProcess(QnaDto qnaBoard, MultipartHttpServletRequest multipart) throws Exception {
    /*ModelAndView mv = new ModelAndView("qna/qnaWrite");*/
    qnaService.insertQna(qnaBoard, multipart);

    return "redirect:/qna/list";
  }

  /* qna 게시글 수정 뷰 */
  @RequestMapping(value = "/update/{qnaPk}", method = RequestMethod.GET)
  public ModelAndView qnaUpdate(@PathVariable("qnaPk") int qnaPk) throws Exception {
    ModelAndView mv = new ModelAndView("qna/qnaUpdate");

    QnaDto qnaBoard = qnaService.selectQnaDetail(qnaPk);
    mv.addObject("qnaBoard", qnaBoard);

    return mv;
  }

  /* qna 게시물 수정 기능 */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public Object boardUpdateProcess(QnaDto qnaBoard) throws Exception{
    int qnaPk = qnaBoard.getQnaPk();
    qnaService.updateQna(qnaBoard);

    // 수정한 페이지로 redirect
    return "redirect:/qna/" + qnaPk;
  }

  /* qna 게시물 삭제 기능 */
  @RequestMapping(value = "/delete/{qnaPk}", method = RequestMethod.GET)
public void qnaDeleteProcess(@PathVariable("qnaPk") int qnaPk, HttpServletResponse response) throws Exception {
    qnaService.deleteQna(qnaPk);
    ScriptUtils.alertAndMovePage(response, "삭제 되었습니다.", "/qna/list");
  }

  // 게시물 다운로드 기능
  @RequestMapping(value = "/downloadQnaFile", method = RequestMethod.GET)
  public void downloadBoardFile(
    // 매개변수 목록
    @RequestParam("qnaFileId") int qnaFileId,
    @RequestParam("qnaPk") int qnaPk,
    HttpServletResponse resp
  ) throws Exception{
    BoardFileDto qnaFile = qnaService.selectQnaFileInfo(qnaFileId, qnaPk);
    if(ObjectUtils.isEmpty(qnaFileId) == false){
      String fileName = qnaFile.getBoardOfileName();
      byte[] files = FileUtils.readFileToByteArray(new File(qnaFile.getBoardSfileName()));

      resp.setContentType("applicaton/octet-stream");
      resp.setContentLength(files.length);
      resp.setHeader("Content-Disposition", "attachment;fileName=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
      resp.getOutputStream().write(files);
      resp.getOutputStream().flush();
      resp.getOutputStream().close();
    }
  }

}
