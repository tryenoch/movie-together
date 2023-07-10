package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.common.ScriptUtils;
import com.bitc.full505_team2_project.dto.BoardDto;
import com.bitc.full505_team2_project.dto.BoardFileDto;
import com.bitc.full505_team2_project.dto.CommentDto;
import com.bitc.full505_team2_project.service.BoardService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping(value = "/board")
public class BoardController {

  @Autowired
  private BoardService boardService;

  /* 게시글 리스트 */
  @RequestMapping(value = {"/list", "/" }, method = RequestMethod.GET)
  public ModelAndView boardList(@RequestParam(required = false, defaultValue = "1") int pageNum) throws Exception {
    ModelAndView mv = new ModelAndView("board/boardList");

    PageInfo<BoardDto> boardList = new PageInfo<>(boardService.selectBoardList(pageNum), 5);

    // List<BoardDto> boardList = boardService.selectBoardList();

    mv.addObject("boardList", boardList);

    return mv;
  }

  /* 게시글 상세 보기 */
  @RequestMapping(value = "{boardPk}", method = RequestMethod.GET)
  public ModelAndView boardDetail(@PathVariable("boardPk") int boardPk) throws Exception{
    ModelAndView mv = new ModelAndView("board/boardDetail");

    BoardDto board = boardService.selectBoardDetail(boardPk);
    // 댓글 리스트
    List<CommentDto> commentList = boardService.selectCommentList(boardPk);

    mv.addObject("board", board);
    mv.addObject("commentList", commentList);

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

  /* comment 입력하기 */
  @RequestMapping(value = "/cmt/write", method = RequestMethod.POST)
  public String qnaCommentInsertProcess(CommentDto comment) throws Exception {
    boardService.insertComment(comment);
    int boardPk = comment.getCommentNum();
    return "redirect:/board/" + boardPk;
  }

  /* comment 삭제하기 */
  /* 자바스크립트로 전달받은 get 변수 형변환을 하려면 PathVariable 이 좋다.*/
  @GetMapping(value = "/cmt/delete/{boardPk}/{commentPk}")
  public String qnaCommentDeleteProcess(@PathVariable("boardPk") int boardPk, @PathVariable("commentPk") int commentPk) throws Exception {
    boardService.deleteComment(boardPk, commentPk);
    return "redirect:/board/" + boardPk; // 삭제후 해당 게시글로 이동
  }

  // 게시물 다운로드 기능
  @RequestMapping(value = "/downloadBoardFile", method = RequestMethod.GET)
  public void downloadBoardFile(
          // 매개변수 목록
          @RequestParam("boardFileId") int boardFileId,
          @RequestParam("boardPk") int boardPk,
          HttpServletResponse resp
  ) throws Exception{
    BoardFileDto boardFile = boardService.selectBoardFileInfo(boardFileId, boardPk);
    if(ObjectUtils.isEmpty(boardFile) == false){
      String fileName = boardFile.getBoardOfileName();
      byte[] files = FileUtils.readFileToByteArray(new File(boardFile.getBoardSfileName()));

      resp.setContentType("applicaton/octet-stream");
      resp.setContentLength(files.length);
      resp.setHeader("Content-Disposition", "attachment;fileName=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
      resp.getOutputStream().write(files);
      resp.getOutputStream().flush();
      resp.getOutputStream().close();
    }
  }

}
